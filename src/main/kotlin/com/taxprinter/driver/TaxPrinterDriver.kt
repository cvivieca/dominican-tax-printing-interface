package com.taxprinter.driver

/**
 * This interface should be implemented by all the printer drivers to be
 * used by the application.
 * Created by george on 04/07/16.
 */
import com.taxprinter.models.*
import org.joda.time.DateTime

interface TaxPrinterDriver {
    fun getPrinterInfo(): PrinterInfo
    fun getVersion(): Version
    fun getState(): State // Gets printer state
    fun printXReport() // Prints X Report
    fun feedPaper() // Feed paper
    fun zClose(withPrint: Boolean): ZClose // Z Close
    fun printInvoice(invoice: Invoice): Boolean // Print an invoice
    fun printLastInvoice(): Boolean // print the last printed invoice
    fun dailyBook(from: DateTime, to: DateTime): String
}