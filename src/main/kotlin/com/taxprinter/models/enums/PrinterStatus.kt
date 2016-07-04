package com.taxprinter.models.enums

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Created by george on 03/07/16.
 */

enum class Cover(@get:JsonValue val s: String) {
    OPEN("open"),
    CLOSED("closed")
}

enum class PrinterState(@get:JsonValue val s: String) {
    ONLINE("online"),
    OFFLINE("offline")
}

enum class Errors(@get:JsonValue val s: String) {
    ERRORS("errors"),
    NONE("none")
}

enum class Moneybox(@get:JsonValue val s: String) {
    OPEN("open"),
    CLOSED("closed")
}

enum class PrinterKind(@get:JsonValue val s: String) {
    RECEIPT("receipt"),
    SLIP("slip"),
    VALIDATION("validation"),
    MICR("MICR")
}
