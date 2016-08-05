package com.taxprinter.resources

import com.google.inject.Inject
import com.google.inject.name.Named
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.models.Invoice
import com.taxprinter.models.Response
import com.taxprinter.services.RequestQueueService
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
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
@Produces(MediaType.APPLICATION_JSON)
class InvoiceResource
@Inject
constructor(@Named("printerDriver") private val driver: TaxPrinterDriver){
    /**
     * Receives an Invoice object over POST; prints an invoice once validated.
     *
     * @param invoice: a valid [Invoice] object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun printInvoice(@Valid invoice: Invoice,
                     @Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.queueRequest {
            Response(
                    "Invoice printed successfully",
                    driver.printInvoice(invoice),
                    "success")
        }(asyncResponse)
    }
}