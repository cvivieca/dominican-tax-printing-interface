package com.taxprinter.driver.bixolonsrp350.utils

import com.taxprinter.models.Invoice

/**
 * Created by george on 05/07/16.
 */
interface Client {
    fun openPort(): Boolean
    fun closePort()
    fun feedPaper()
    fun getVersion(): String
    fun printInvoice(invoice: Invoice): Boolean
    fun printLastInvoice(): Boolean
    fun closeZReport(withPrint: Boolean)
    fun closeXReport()
    fun getStatusS1(): ByteArray
    fun getStatusS2(): ByteArray
    fun getState(): ByteArray
}