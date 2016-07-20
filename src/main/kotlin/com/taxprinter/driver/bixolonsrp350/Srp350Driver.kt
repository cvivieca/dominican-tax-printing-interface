package com.taxprinter.driver.bixolonsrp350

import com.google.inject.Inject
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.driver.bixolonsrp350.utils.SerialClient
import com.taxprinter.models.FiscalStatus
import com.taxprinter.models.PrinterStatus
import com.taxprinter.models.State
import com.taxprinter.models.Version
import com.taxprinter.models.enums.*
import org.apache.log4j.Logger

/**
 * This class represents the driver for the Bixolon SRP-350 driver
 * Created by george on 04/07/16.
 */
class Srp350Driver
@Inject
constructor(val client: SerialClient) : TaxPrinterDriver {
    companion object {
        val logger = Logger.getLogger(Srp350Driver::class.java)
    }

    override fun printXReport() {

    }

    override fun getState(): State {
        client.openPort()
        val stateByteArray = client.getState().slice(1..2)
                .toByteArray()
        val statusByteArray = client.getStatus()
        client.closePort()
        return State(
                FiscalStatus(
                        DocumentType.NONE,
                        getMemoryStatus(stateByteArray.getOrNull(1)?: 0),
                        getMode(stateByteArray.getOrNull(1)?: 0),
                        SubState.FISCALAUDITORY,
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
        return Version(client.version, "Enterprise Tax Printer Server " +
                "- SRP-350 driver")
    }
}