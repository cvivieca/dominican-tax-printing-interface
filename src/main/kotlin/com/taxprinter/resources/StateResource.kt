package com.taxprinter.resources

import com.google.inject.Inject
import com.google.inject.name.Named
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.models.Response
import com.taxprinter.services.RequestQueueService
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType

/**
 * Created by george on 03/07/16.
 */

/**
 * JAX-RS resource returns tax-printer information and internal fiscal status
 */
@Path("/state")
@Produces(MediaType.APPLICATION_JSON)
class StateResource
@Inject
constructor(@Named("printerDriver") private val driver: TaxPrinterDriver) {
    /**
     * Returns tax-printer information
     * @return Tax Printer information
     */
    @GET
    fun state(@Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.queueRequest {
            Response(
                    "",
                    driver.getState()
                    ,
                    "success")
        }(asyncResponse)
    }
}