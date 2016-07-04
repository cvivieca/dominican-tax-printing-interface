package com.taxprinter.resources

import com.taxprinter.models.FiscalStatus
import com.taxprinter.models.PrinterStatus
import com.taxprinter.models.Response
import com.taxprinter.models.State
import com.taxprinter.models.enums.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Created by george on 03/07/16.
 */

/**
 * JAX-RS resource returns tax-printer information and internal fiscal status
 */
@Path("/state")
@Produces(MediaType.APPLICATION_JSON)
class StateResource {

    /**
     * Returns tax-printer information
     * @return Tax Printer information
     */
    @GET
    fun state(): Response<State> = Response(
            "",
            State(
                    FiscalStatus(
                            DocumentType.NONE,
                            MemoryStatus.GOOD,
                            Mode.TRAINING,
                            SubState.FISCALAUDITORY,
                            false,
                            false),
                    PrinterStatus(
                            Cover.CLOSED,
                            PrinterState.ONLINE,
                            Errors.NONE,
                            Moneybox.CLOSED,
                            PrinterKind.RECEIPT)
            ),
            "success")
}