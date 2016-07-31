package com.taxprinter.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.validation.ValidationMethod
import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.valuehandling.UnwrapValidatedValue
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Size

/**
 * Invoice is a simple POJO representing a real invoice coming over the Rest API
 * Created by george on 30/07/16.
 */
class Invoice
@JsonCreator
constructor   (
        @JsonProperty("type") @NotEmpty var type: String,
        @JsonProperty("copy") var copy: Optional<Boolean>,
        @JsonProperty("cashier") var cashier: Optional<Int>,
        @JsonProperty("subsidiary") var subsidiary: Int,
        @JsonProperty("ncf") @Size(min = 19, max = 19) var ncf: Optional<String>,
        @JsonProperty("reference_ncf") var referenceNcf: Optional<String>,
        @JsonProperty("client") var client: Optional<String>,
        @JsonProperty("rnc") var rnc: Optional<String>,
        @JsonProperty("items") @NotEmpty @Valid @UnwrapValidatedValue var items: Array<Item>,
        @JsonProperty("payments") @NotEmpty @Valid @UnwrapValidatedValue var payments: Array<Payment>,
        @JsonProperty("discounts") @Valid @UnwrapValidatedValue var discounts: Optional<Array<Discount>>,
        @JsonProperty("charges") @Valid @UnwrapValidatedValue var charges: Optional<Array<Charge>>,
        @JsonProperty("comments") @NotEmpty @UnwrapValidatedValue var comments: Optional<Array<String>>

) {
    @ValidationMethod(message = "El campo type debe contener valores validos")
    fun isValidType(): Boolean {
        return (type in arrayOf("document", "nofiscal", "final", "fiscal",
                "special", "final_note", "fiscal_note", "special_note"))
    }

    @ValidationMethod(message = "El campo client es obligatorio cuando se emitan facturas con derecho a credito fiscal")
    fun isValidClient(): Boolean {
        return if (type in arrayOf("fiscal", "fiscal_note", "special", "special_note")) client.isPresent else true
    }

    @ValidationMethod(message = "El campo rnc es obligatorio cuando se emitan facturas con derecho a credito fiscal")
    fun isValidRnc(): Boolean {
        return if (type in arrayOf("fiscal", "fiscal_note", "special", "special_note")) rnc.isPresent else true
    }

    @ValidationMethod(message = "El campo comments admite 10 comentarios y cada linea de comentario debe tener 40 chars")
    fun isValidComments(): Boolean {
        return if (comments.isPresent) comments.get().size <= 10 && comments.get().all { it.length <= 40 } else true
    }

    @ValidationMethod(message = "El campo ncf es opcional solo para documentos de no venta")
    fun isValidNcf(): Boolean  {
        return if (type in arrayOf("fiscal", "fiscal_note", "final", "final_note", "special", "special_note"))
            ncf.isPresent else true
    }

    @ValidationMethod(message = "El campo reference_ncf es obligatorio cuando se emita una nota de credito")
    fun isValidReferenceNcf(): Boolean {
        return if (type == "fiscal_note" || type == "final_note" || type == "special_note") (referenceNcf.isPresent)  else true
    }
}