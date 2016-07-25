package com.taxprinter.models.enums

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

/**
 * Created by george on 03/07/16.
 */


fun getDocumentType(k: Byte): String {
    val table = Hashtable<Byte, String>()
    table.put(0x00, "none")
    table.put(0x01, "final")
    table.put(0x02, "fiscal")
    table.put(0x03, "finalcreditnote")
    table.put(0x04, "fiscalcreditnote")
    table.put(0x05, "salespayment")
    table.put(0x06, "creditnotepayment")
    return table[k]?: "unknown"
}

fun getMemoryStatus(k: Byte): String {
    val table = Hashtable<Byte, String>()
    table.put(0b01000000, "good")
    table.put(0b01010000, "depleted")
    table.put(0b01001000, "full")
    // AND with a Mask
    return table[(k.toInt() and 0b01011000).toByte()]?: "unknown"
}

fun getMode(k: Byte): String {
    val table = Hashtable<Byte, String>()
    table.put(0b01100000, "fiscal")
    table.put(0b01000000, "training")
    // AND with a Mask
    return table[(k.toInt() and 0b01100000).toByte()]?: "unknown"
}

fun getSubstate(k: Byte): String {
    val table = Hashtable<Byte, String>()
    table.put(0b01000001, "fiscal_transaction")
    // AND with a Mask
    return table[(k.toInt() and 0b01000001).toByte()]?: "none"
}

enum class SubState(@get:JsonValue val s: String) {
    NONE("none"),
    RESERVED("reserved"),
    SCANNER("scanner"),
    LOGO("logo"),
    FISCALAUDITORY("fiscal_auditory"),
    FISCALTRANSACTION("fiscal_transaction"),
    SLIP("slip")
}