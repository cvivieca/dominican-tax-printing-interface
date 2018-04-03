package com.taxprinter.resources

import com.google.inject.Inject
import com.google.inject.name.Named
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.models.Invoice
import com.taxprinter.models.Response
import com.taxprinter.services.RequestQueueService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType

/**
 * Created by george on 30/07/16.
 * This class represents an Invoice Resource; sends a command to print an
 * invoice.
 *
 * @property driver: An instance of [TaxPrinterDriver] injected by the DI
 * @constructor Receives a [TaxPrinterDriver] instance injected by the DI and
 * creates a new InvoiceResource JAX-RS resource
 */
@Path("/invoice")
@Api(value = "/invoice", description = "Receives an Invoice object; prints the validated invoice")
@Produces(MediaType.APPLICATION_JSON)
class InvoiceResource
@Inject
constructor(private val driver: TaxPrinterDriver){
    /**
     * Receives an Invoice object over POST; prints an invoice once validated.
     *
     * @param invoice: a valid [Invoice] object
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Prints an invoice",
            notes = "Accepts camel cased fields and underscore as well (field_name).",
            response = Response::class)
    fun printInvoice(@Valid invoice: Invoice,
                     @Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.runRequestAsync {
            Response(
                    "Invoice printed successfully",
                    driver.printInvoice(invoice),
                    "success")
        }(asyncResponse)
    }

    /**
     * Prints the last printed Invoice (uses printer capabilities)
     */
    @GET
    @Path("/last")
    fun printLast(@Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.runRequestAsync {
            Response(
                    "Last invoice printed again",
                    driver.printLastInvoice(),
                    "success"
            )
        }(asyncResponse)

    }
}