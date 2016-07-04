package com.taxprinter.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by george on 03/07/16.
 */

/**
 * Immutable POJO which represents the Printer State
 */

class State(
        @get:JsonProperty("fiscal_status") val fiscalStatus: FiscalStatus,
        @get:JsonProperty("printer_status") val printerStatus: PrinterStatus
)