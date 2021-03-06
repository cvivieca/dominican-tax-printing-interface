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
@Path("/printer_information")
@Produces(MediaType.APPLICATION_JSON)
class PrinterInformationResource
@Inject
constructor(private val driver: TaxPrinterDriver) {
    /**
     * Returns printer hardware info
     * @return Printer Hardware Info
     */
    @GET
    fun state(@Suspended asyncResponse: AsyncResponse) {
        RequestQueueService.runRequestAsync {
            Response(
                    "",
                    driver.getPrinterInfo()
                    ,
                    "success")
        }(asyncResponse)
    }
}