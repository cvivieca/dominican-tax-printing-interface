package com.taxprinter.services

import com.taxprinter.models.Response
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.function.Supplier
import javax.ws.rs.container.AsyncResponse

/**
 * RequestQueueService is a class to manage access to the printer driver layer
 * Created by george on 07/07/16.
 */

object RequestQueueService {

    val ex: Executor = Executors.newSingleThreadExecutor()

    fun <T> runRequestAsync(body: () -> Response<T>): (AsyncResponse) -> Unit {
        return { asyncResponse: AsyncResponse ->
            CompletableFuture.supplyAsync(Supplier(body), ex) // Had to cast explicitly to java supplier
            .thenApply { asyncResponse.resume(it)}
        }
    }
}