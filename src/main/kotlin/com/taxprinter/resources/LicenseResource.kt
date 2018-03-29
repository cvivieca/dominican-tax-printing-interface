package com.taxprinter.resources


import com.taxprinter.boot.License
import com.taxprinter.models.Hardware

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/license")
@Produces(MediaType.APPLICATION_JSON)
class LicenseResource {

    val hardwareId: Hardware
        @GET
        get() = License.uniqueId()
}
