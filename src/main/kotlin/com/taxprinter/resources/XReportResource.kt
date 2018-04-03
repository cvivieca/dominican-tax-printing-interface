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
 * This resource return an X report
 * Created by george on 22/07/16.
 */
@Path("/x")
@Produces(MediaType.APPLICATION_JSON)
class XReportResource
@Inject
constructor(private val driver: TaxPrinterDriver) {
    /**
     * Do a fiscal end by doing a Z close
     * @return ZClose
     */
    @GET
    fun close(@Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.runRequestAsync {
            Response(
                    "",
                    driver.printXReport()
                    ,
                    "success")
        }(asyncResponse)
    }
}