package com.taxprinter.resources

import com.taxprinter.models.Response
import com.taxprinter.models.Version
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Created by george on 03/07/16.
 *
 * This source file holds the PrinterResource class
 */


/**
 * JAX-RS resource to get Software Version
 */
@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
class VersionResource() {
    /**
     * Gets the API software compatible version (ver 7.0-pre at this time).
     */
    @GET
    fun version(): Response<Version> = Response(
            "",
            Version("7.0-pre", "Tax Printer Interface"),
            "success")
}
