package com.taxprinter.resources

import com.google.inject.Inject
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.models.Response
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType

/**
 * Created by george on 03/07/16.
 *
 * This source file holds the PrinterResource class
 */


/**
 * JAX-RS resource to get Software Version
 */

@Path("/software_version")
@Produces(MediaType.APPLICATION_JSON)
class VersionResource
@Inject
constructor(private val driver: TaxPrinterDriver): ParentResource() {
    /**
     * Gets the API software compatible version (ver 7.0-pre at this time).
     */
    @GET
    fun version(@Suspended asyncResponse: AsyncResponse) {
        runWithHwLock {
            Response(
                    "",
                    driver.getVersion(),
                    "success")
        }(asyncResponse)
    }
}
