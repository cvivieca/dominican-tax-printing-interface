package com.taxprinter.driver.bixolonsrp350

import com.google.inject.Inject
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.driver.bixolonsrp350.utils.Client
import com.taxprinter.models.*
import com.taxprinter.models.enums.*
import org.apache.log4j.Logger

/**
 * This class represents the driver for the Bixolon SRP-350 driver
 * Created by george on 04/07/16.
 */
class Srp350Driver
@Inject
constructor(val client: Client) : TaxPrinterDriver {

    override fun printInvoice(invoice: Invoice): Boolean {
        client.openPort()
        val print = client.printInvoice(invoice)
        client.closePort()
        return print
    }

    override fun zClose(withPrint: Boolean): ZClose {
        client.openPort()
        client.closeZReport(withPrint)
        client.closePort()
        return ZClose(12)
    }

    companion object {
        val logger = Logger.getLogger(Srp350Driver::class.java)
    }

    override fun printXReport() {
        client.openPort()
        client.closeXReport()
        client.closePort()

    }

    override fun feedPaper() {
        client.openPort()
        client.feedPaper()
        client.closePort()
    }

    override fun getPrinterInfo(): PrinterInfo {
        client.openPort()
        val statusS1ByteArray = client.getStatusS1()
        val printerSerialBytes = statusS1ByteArray.slice(131..136)
            .toByteArray()
        client.closePort()
        val printerSerial = String(printerSerialBytes, charset("ASCII"))
        return PrinterInfo(printerSerial, printerSerial)

    }

    override fun getState(): State {
        // TODO: Finish this!
        client.openPort()
        val stateByteArray = client.getState().slice(1..2)
                .toByteArray()
        val statusByteArray = client.getStatusS2()
        val documentTypeBytes = statusByteArray[75]
        client.closePort()
        return State(
                FiscalStatus(
                        getDocumentType(documentTypeBytes),
                        getMemoryStatus(stateByteArray.getOrNull(1)?: 0),
                        getMode(stateByteArray.getOrNull(1)?: 0),
                        getSubstate(stateByteArray.getOrNull(1)?: 0),
                        false,
                        false),
                PrinterStatus(
                        Cover.CLOSED,
                        PrinterState.ONLINE,
                        Errors.NONE,
                        Moneybox.CLOSED,
                        PrinterKind.RECEIPT)
        )
    }

    override fun getVersion(): Version {
        return Version(client.getVersion(), "Enterprise Tax Printer Server " +
                "- SRP-350 driver")
    }
}