package com.taxprinter.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.validation.ValidationMethod
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.constraints.DecimalMin
import java.util.*

/**
 * Payment represent a Payment class, holding information about how to pay an item
 * Created by george on 30/07/16.
 */
data class Payment
@JsonCreator
constructor(
        @JsonProperty("type") @field:NotEmpty var type: String,
        @JsonProperty("amount") @field:DecimalMin("0.01") var amount: Double,
        @JsonProperty("description") var description: Optional<String>
) {
    @ValidationMethod(message = "El campo type debe contener uno de los siguientes valores: " +
            "cash, check, credit_card, debit_card, card, coupon, other, credit_note")
    fun isValidType(): Boolean {
        return (type in arrayOf("cash","check", "credit_card", "debit_card",
                "card", "coupon", "other", "credit_note"))
    }
}