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
 * This resource closes a fiscal cycle by doing a Z Close
 * Created by george on 22/07/16.
 */
@Path("/zclose")
@Produces(MediaType.APPLICATION_JSON)
class ZCloseResource
@Inject
constructor(@Named("printerDriver") private val driver: TaxPrinterDriver){
    /**
     * Do a fiscal end by doing a Z close
     * @return ZClose
     */
    @GET
    @Path("/")
    fun close(@Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.runRequestAsync {
            Response(
                "",
                driver.zClose(withPrint = false)
                ,
                "success")
        }(asyncResponse)
    }
    /**
     * Do a fiscal end by doing a Z close with a print
     * @return ZClose
     */
    @GET
    @Path("/print")
    fun closeWithPrint(@Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.runRequestAsync {
            Response(
                "",
                driver.zClose(withPrint = true)
                ,
                "success")
        }(asyncResponse)
    }
}