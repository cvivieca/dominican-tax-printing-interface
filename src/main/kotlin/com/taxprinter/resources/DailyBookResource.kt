package com.taxprinter.resources

import com.google.inject.Inject
import com.google.inject.name.Named
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.services.RequestQueueService
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier
import javax.ws.rs.*
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.StreamingOutput

/**
 * JAX-RS resource to print dailybooks
 * Created by george on 18/11/16.
 */
@Path("/daily_book")
class DailyBookResource
@Inject
constructor(private val driver: TaxPrinterDriver){

    val format: DateTimeFormatter = DateTimeFormat.forPattern("ddMMyy")

    @GET
    @Produces("text/plain")
    fun dailyBook(@QueryParam("from") pFrom: String,
                  @QueryParam("to") pTo: String,
                  @Suspended asyncResponse: AsyncResponse) {
        val from = format.parseDateTime(pFrom)
        val to = format.parseDateTime(pTo)

        CompletableFuture.supplyAsync(Supplier({
            val report = driver.dailyBook(from, to)
            val stream = StreamingOutput { output ->
                output.write(report.toByteArray())
                output.flush()
            }
            Response
                .ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = report.txt")
                .build()
        }), RequestQueueService.ex)
                .thenApply { asyncResponse.resume(it)}

    }
}