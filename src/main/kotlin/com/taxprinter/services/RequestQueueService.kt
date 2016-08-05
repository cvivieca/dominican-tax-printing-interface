package com.taxprinter.services

import com.taxprinter.models.Response
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import javax.ws.rs.container.AsyncResponse

/**
 * RequestQueueService is a class to manage access to the printer driver layer
 * Created by george on 07/07/16.
 */

object RequestQueueService {

    private val resourceExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val queue = ConcurrentLinkedQueue<() -> Unit>()

    fun startRunner() {
        resourceExecutor.submit {
            while (true) {
                queue.poll()?.invoke()
                Thread.sleep(200)
            }
        }
    }


    fun <T> queueRequest(body: () -> Response<T>): (AsyncResponse) -> Unit {
        return { asyncResponse: AsyncResponse ->
                val result = body()
                if (queue.size > 13) {
                    val result = Response(
                            "Request queue full.",
                            null
                            ,
                            "error")
                    asyncResponse.resume(result)
                } else {
                    queue.add { asyncResponse.resume(result) }
                }
        }
    }
}