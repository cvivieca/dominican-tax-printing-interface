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
 * JAX-RS resource providing information about the current printer
 * Created by george on 21/07/16.
 */
@Path("/advance_paper")
@Produces(MediaType.APPLICATION_JSON)
class FeedPaperResource
@Inject
constructor(@Named("printerDriver") private val driver: TaxPrinterDriver){
    /**
     * Feed paper
     * @return FeedPaper
     */
    @GET
    fun feed(@Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.queueRequest {
            Response(
                    "Hardware busy.",
                    null
                    ,
                    "error")
        }(asyncResponse)
    }
}