package com.taxprinter.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMin

/**
 *
 * Created by george on 30/07/16.
 */
data class Charge
@JsonCreator
constructor(
        @JsonProperty("amount") @field:DecimalMin("0.01") var amount: Double,
        @JsonProperty("description") var description: String
)