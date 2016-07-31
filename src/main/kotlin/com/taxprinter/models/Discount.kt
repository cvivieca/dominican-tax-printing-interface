package com.taxprinter.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * This class represents a Discount object applied to a Invoice
 * Created by george on 30/07/16.
 */
data class Discount
@JsonCreator
constructor(
        @JsonProperty("amount")
        @field:javax.validation.constraints.DecimalMin("0.00")
        @field:javax.validation.constraints.DecimalMax("99.99")
        var amount: Double,
        @JsonProperty("description") var description: String)