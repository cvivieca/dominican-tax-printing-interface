package com.taxprinter.models.enums

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

/**
 * Created by george on 03/07/16.
 */
// Applied @get annotation to JsonValue cause it refuses to annotate
// parameters even when kotlin generates a getter out of it, so I
// explicitly put @get for getter, JsonValue over the getter
// specifies that variable will be used as a representation for Jackson
enum class DocumentType(@get:JsonValue val s: String) {
    NONE("none"),
    FINAL("final"),
    FISCAL("fiscal"),
    NOFISCAL("nofiscal"),
    REPORT("report");
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

enum class SubState(@get:JsonValue val s: String) {
    NONE("none"),
    RESERVED("reserved"),
    SCANNER("scanner"),
    LOGO("logo"),
    FISCALAUDITORY("fiscal_auditory"),
    FISCALTRANSACTION("fiscal_transaction"),
    SLIP("slip")
}