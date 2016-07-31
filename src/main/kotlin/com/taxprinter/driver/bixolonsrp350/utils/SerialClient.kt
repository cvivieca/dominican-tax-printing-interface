package com.taxprinter.driver.bixolonsrp350.utils

import com.fazecast.jSerialComm.SerialPort
import com.google.inject.Inject
import com.google.inject.name.Named
import com.taxprinter.models.Invoice
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import java.io.InputStream
import java.util.*
import java.util.concurrent.*


/**
 * This class is responsible of the correct low level communication with
 * the printer
 * Created by george on 05/07/16.
 */
class SerialClient
@Inject constructor(@Named("portDescriptor") val portDescriptor: String) : Client {

    companion object {
        val logger: Logger = Logger.getLogger(SerialClient::class.java)
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
    }

    private fun safeRead(input: InputStream?,
                         size: Int,
                         timeout: Long): ByteArray {
        if (input == null) return ByteArray(size) // Safe check of input
        val ft = executor.submit( Callable {
            IOUtils.toByteArray(input, size)
        })
        return try {ft.get(timeout, TimeUnit.SECONDS)} catch (_: TimeoutException) {ByteArray(size)}

    }

    fun queryCmd(bytePayload: ByteArray): Boolean {
        val frame = prepareFrame(bytePayload)
        val bytesWritten = comPort.writeBytes(frame, frame.size.toLong())
        logger.info("Wrote ${bytesWritten} bytes.")
        return true
    }

    override fun printInvoice(invoice: Invoice): Boolean {
        val errTable = Hashtable<Byte, String>()
        errTable.put(0x06, "Command executed successfully.")
        errTable.put(0x15, "Error running command.")

        val documentType = Hashtable<String, Byte>()
        documentType.put("final", 0x30)
        documentType.put("fiscal", 0x31)
        documentType.put("final_note", 0x32)
        documentType.put("fiscal_note", 0x33)
        documentType.put("special", 0x34)
        documentType.put("special_note", 0x35)
        documentType.put("document", 0x36)
        documentType.put("nofiscal", 0x36)

        // Write customer info for a document except for document and nonfiscal types
        if (invoice.type !in arrayOf("document", "nofiscal")) {
            val rncb = invoice.rnc.orElse("").toByteArray(charset("ASCII"))
            val clientb = invoice.client.orElse("").toByteArray(charset("ASCII"))
            val rncFrame = prepareFrame(byteArrayOf(0x69, 0x52, 0x30) + rncb)
            val clientFrame = prepareFrame(byteArrayOf(0x69, 0x53, 0x30) + clientb)
            comPort.writeBytes(rncFrame, rncFrame.size.toLong())
            logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")

            comPort.writeBytes(clientFrame, clientFrame.size.toLong())
            logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
        }

        if (invoice.ncf.isPresent) {
            // Write ncf if present
            val ncfb = byteArrayOf(0x46) + invoice.ncf.orElse("").padStart(19, '0').toByteArray(charset("ASCII"))
            val ncfFrame = prepareFrame(ncfb)
            comPort.writeBytes(ncfFrame, ncfFrame.size.toLong())
            logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
        }

        if (invoice.referenceNcf.isPresent) {
            // write referenceNcf if present
            val rncf = byteArrayOf(0x69, 0x46, 0x30) + invoice.referenceNcf.orElse("").padStart(19, '0').toByteArray(charset("ASCII"))
            val rncFrame = prepareFrame(rncf)
            comPort.writeBytes(rncFrame, rncFrame.size.toLong())
            logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
        }

        // Write document type
        val dt = byteArrayOf(0x2f, documentType[invoice.type]?: 0x30)
        val dtFrame = prepareFrame(dt)
        comPort.writeBytes(dtFrame, dtFrame.size.toLong())
        logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")

        val taxb = Hashtable<Int, Byte>()
        taxb.put(0, 0x20)
        taxb.put(5, 0x21)
        taxb.put(8, 0x22)
        taxb.put(11, 0x23)
        taxb.put(13, 0x24)
        taxb.put(18, 0x25)

        // Register items
        for ((description, extraDescription, quantity, price, itbis, discount) in invoice.items) {
            val taxba = byteArrayOf(taxb[itbis]?: 0x20)
            val priceb = (price * 100).toInt().toString().padStart(10, '0').toByteArray(charset("ASCII"))
            val qtyb = (quantity * 100).toInt().toString().padStart(8, '0').toByteArray(charset("ASCII"))
            val descb = description.toByteArray(charset("ASCII"))
            val itemframe = prepareFrame(taxba + priceb + qtyb + descb)
            comPort.writeBytes(itemframe, itemframe.size.toLong())
            logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
            val itd = byteArrayOf(0x70, 0x2d) + (discount * 100).toInt().toString().padStart(4, '0').toByteArray(charset("ASCII"))
            val itdFrame = prepareFrame(itd)
            comPort.writeBytes(itdFrame, itdFrame.size.toLong())
            logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
        }

        // Register discounts
        for ((amount) in invoice.discounts.orElse(emptyArray())) {
            val perc = byteArrayOf(0x70, 0x2a) + (amount * 100).toInt().toString().padStart(4, '0').toByteArray(charset("ASCII"))
            comPort.writeBytes(perc, perc.size.toLong())
            logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
        }

        val pmt = Hashtable<String, ByteArray>()
        pmt.put("cash", byteArrayOf(0x30, 0x31))
        pmt.put("check", byteArrayOf(0x30, 0x32))
        pmt.put("credit_card", byteArrayOf(0x30, 0x33))
        pmt.put("debit_card", byteArrayOf(0x30, 0x34))
        pmt.put("credit_note", byteArrayOf(0x30, 0x35))
        pmt.put("coupon", byteArrayOf(0x30, 0x36))
        pmt.put("card", byteArrayOf(0x30, 0x38))
        pmt.put("other", byteArrayOf(0x30, 0x39))

        // Payments register
        invoice.payments.forEachIndexed { i, payment ->
            if (i == invoice.payments.size - 1) {
                val payl = byteArrayOf(0x31) + (pmt[payment.type]?: byteArrayOf(0x31))
                val paylFrame = prepareFrame(payl)
                comPort.writeBytes(paylFrame, paylFrame.size.toLong())
                logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
            } else {
                val payb = pmt[payment.type] ?: byteArrayOf(0x31)
                val amountb = (payment.amount * 100).toInt().toString().padStart(12, '0').toByteArray(charset("ASCII"))
                val commentb = payment.description.orElse("").toByteArray(charset("ASCII"))
                val pmntpayload = byteArrayOf(0x32) + payb + amountb + commentb
                val pmntPayloadFrame = prepareFrame(pmntpayload)
                comPort.writeBytes(pmntPayloadFrame, pmntPayloadFrame.size.toLong())
                logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
            }
        }

        // TODO: Messages or comment

        val closedb = byteArrayOf(0x31, 0x39, 0x39)
        val closedbFrame = prepareFrame(closedb)
        comPort.writeBytes(closedbFrame, closedbFrame.size.toLong())
        logger.debug("ACK: ${safeRead(comPort.inputStream, 1, 1)}")
        return true

    }

    override fun closeZReport(withPrint: Boolean) {
        if (withPrint) {
            queryCmd(byteArrayOf(0x49, 0x30, 0x5A, 0x30))
            return
        }
        queryCmd(byteArrayOf(0x49, 0x30, 0x5A, 0x31))
    }

    override fun closeXReport() {
        queryCmd(byteArrayOf(0x49, 0x30, 0x58))
    }

    override fun feedPaper() {
        // TODO: Make this work!
        // comPort.writeBytes(byteArrayOf(0x07, 0x01), 2)
    }

    override fun getStatusS1(): ByteArray {
        getState() // Send ENQ to enable error reporting
        queryCmd(byteArrayOf(0x53, 0x31))
        val input = comPort.inputStream
        return safeRead(input, 137, 3)
    }

    override fun getStatusS2(): ByteArray {
        getState() // Send ENQ to enable error reporting
        queryCmd(byteArrayOf(0x53, 0x32))
        val input = comPort.inputStream
        return safeRead(input, 79, 3)
    }

    override fun getState(): ByteArray {
        comPort.writeBytes(byteArrayOf(0x05), 1)
        // Collect data which arrived at hardware port buffer
        val input: InputStream? = comPort.inputStream
        return safeRead(input, 4, 2)
    }

    override fun getVersion(): String { return "Serial Client v0.1" }

    val comPort = SerialPort.getCommPort(portDescriptor)

    override fun openPort(): Boolean {
        logger.info("Openning port: ${portDescriptor}")
        comPort.openPort()
        comPort.baudRate = 9600
        comPort.numDataBits = 8
        comPort.numStopBits = 1
        comPort.parity = SerialPort.EVEN_PARITY
        comPort.setFlowControl(SerialPort.FLOW_CONTROL_CTS_ENABLED)
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 100, 0)
        if (!comPort.isOpen) {
            logger.error("Cannot open port!")
            return false
        }
        logger.info("Port open!")
        return true
    }

    override fun closePort() {
        logger.info("Closing port: ${portDescriptor}")
        comPort.closePort()
        logger.info("Port closed!")
    }

    private fun createLRC(line: ByteArray): Byte {
        val lrcLine = ByteArray(line.size + 1)
        line.forEachIndexed { i, c -> lrcLine[i] = c }
        lrcLine[lrcLine.size-1] = 0x03 // Add ETX char (end of transmission)
        // generate LRC by doing a XOR between all elements of the LRC Line
        // Kotlin only supports XOR on Int and Long so we convert the char byte
        // to Int
        return lrcLine.map { it.toInt() }.reduce { i, acc ->  i xor acc}.toByte()
    }

    private fun prepareFrame(bytePayload: ByteArray): ByteArray {
        // Prepare the LRC which is the payload plus ETX char byte
        val lrc = createLRC(bytePayload)
        // Prepare the frame, the bytePayload(DATA) size plus 3 special char
        // bytes (STX, ETX, LRC)
        val frame = ByteArray(bytePayload.size + 3)
        // DATA a sequence of data at its correct position in the frame
        bytePayload.forEachIndexed { i, bytes -> frame[i+1] = bytes }
        frame[0] = 0x02 // STX char (start of transmission)
        frame[frame.size-1] = lrc.toByte() // LRC (used as a check mechanism)
        frame[frame.size-2] = 0x03 // ETX char end of transmission
        return frame
    }

}