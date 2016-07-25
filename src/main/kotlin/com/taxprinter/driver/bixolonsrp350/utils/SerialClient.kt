package com.taxprinter.driver.bixolonsrp350.utils

import com.fazecast.jSerialComm.SerialPort
import com.google.inject.Inject
import com.google.inject.name.Named
import com.sun.org.apache.xpath.internal.operations.Bool
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import javax.validation.Payload

/**
 * This class is responsible of the correct low level communication with
 * the printer
 * Created by george on 05/07/16.
 */
class SerialClient
@Inject constructor(@Named("portDescriptor") val portDescriptor: String) : Client {
    override fun queryCmd(bytePayload: ByteArray): Boolean {
        val frame = prepareFrame(bytePayload)
        val bytesWritten = comPort.writeBytes(frame, frame.size.toLong())
        logger.info("Wrote ${bytesWritten} bytes.")
        return true
    }

    fun closeZReport(withPrint: Boolean) {
        if (withPrint) {
            queryCmd(byteArrayOf(0x49, 0x30, 0x5A, 0x30))
            return
        }
        queryCmd(byteArrayOf(0x49, 0x30, 0x5A, 0x31))
    }

    fun closeXReport() {
        queryCmd(byteArrayOf(0x49, 0x30, 0x58))
    }

    fun feedPaper() {
        // TODO: Make this work!
        // comPort.writeBytes(byteArrayOf(0x07, 0x01), 2)
    }

    fun getStatusS1(): ByteArray {
        getState() // Send ENQ to enable error reporting
        queryCmd(byteArrayOf(0x53, 0x31))
        val input = comPort.inputStream
        // Read until 150 bytes, this fucking stream interface never ends
        val bytes = IOUtils.toByteArray(input, 150)
        return bytes
    }

    fun getStatusS2(): ByteArray {
        getState() // Send ENQ to enable error reporting
        queryCmd(byteArrayOf(0x53, 0x32))
        val buffer = ByteArray(1024)
        comPort.readBytes(buffer, buffer.size.toLong())
        return buffer
    }

    fun getState(): ByteArray {
        comPort.writeBytes(byteArrayOf(0x05), 1)
        val buffer = ByteArray(1024)
        // Collect data which arrived at hardware port buffer
        comPort.readBytes(buffer, buffer.size.toLong())
        return buffer
    }

    override fun readFpStatus(): Int {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun simpleCmd(bytePayload: ByteArray): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val logger = Logger.getLogger(SerialClient::class.java)
    }

    val version = "Serial Client v0.1"

    val comPort = SerialPort.getCommPort(portDescriptor)

    fun openPort(): Boolean {
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

    fun closePort() {
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

    override fun fetchRow(): ByteArray {
        val buffer = ByteArray(1024)
        while (true) {
            Thread.sleep(7000)
            val readBytes = comPort.readBytes(buffer, buffer.size.toLong())
            if (readBytes > 3) {
                val incomingLine = buffer.slice(1..-1).toByteArray()
                val incomingLRC = createLRC(incomingLine)
                if (incomingLRC == buffer[-1]) {
                    val ack = byteArrayOf(0x06)
                    comPort.writeBytes(ack, ack.size.toLong())
                } else {
                    val noAck = byteArrayOf(0x15)
                    comPort.writeBytes(noAck, noAck.size.toLong())
                }
            } else {
                break
            }
        }
        return buffer
    }

}