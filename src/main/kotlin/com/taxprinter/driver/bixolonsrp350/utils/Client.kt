package com.taxprinter.driver.bixolonsrp350.utils

/**
 * Created by george on 05/07/16.
 */
interface Client {
    fun queryCmd(bytePayload: ByteArray): Boolean
    fun fetchRow(): ByteArray
    fun readFpStatus(): Int
    fun simpleCmd(bytePayload: ByteArray): Boolean
}