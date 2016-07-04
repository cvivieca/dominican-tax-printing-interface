package com.taxprinter.models.enums

import com.fasterxml.jackson.annotation.JsonValue

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

enum class MemoryStatus(@get:JsonValue val s: String) {
    GOOD("good"),
    DEPLETED("depleted"),
    FULL("full"),
    BROKEN("broken")
}

enum class Mode(@get:JsonValue val s: String) {
    BLOCKED("blocked"),
    MANUFACTURE("manufacture"),
    TRAINING("training"),
    FISCAL("fiscal")
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