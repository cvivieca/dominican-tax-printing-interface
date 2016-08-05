package com.taxprinter.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.validation.ValidationMethod
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import java.util.*

/**
 * A class representing an item on an invoice
 * Created by george on 30/07/16.
 */
data class Item
@JsonCreator
constructor(
        @JsonProperty("description") @field:Max(127) var description: String,
        @JsonProperty("extra_description") var extraDescription: Optional<String>,
        @JsonProperty("quantity") @field:Min(0) var quantity: Double,
        @JsonProperty("price") @field:Min(0) var price: Double,
        @JsonProperty("itbis") @field:Min(0) var itbis: Int,
        @JsonProperty("discount") @field:Max(100) var discount: Optional<Double>,
        @JsonProperty("charges") @field:Min(100) var charges: Double
        ) {
    @ValidationMethod(message = "El campo itbis solo acepta uno de los siguientes valores: 18, 13, 11, 8, 5, 0")
    fun isValidItbis(): Boolean {
        return (itbis in arrayOf(18, 13, 11, 8, 5, 0))
    }
}