package com.taxprinter.resources

import com.taxprinter.models.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import javax.ws.rs.container.AsyncResponse

/**
 * ParentResource is a class to manage locks to the printer hardware
 * Created by george on 07/07/16.
 */
open class ParentResource {
    companion object {
        private val resourceExecutor: ExecutorService = Executors.newFixedThreadPool(Thread.activeCount())
        private val lock = ReentrantLock()
    }

    fun <T> runWithHwLock(body: () -> Response<T>): (AsyncResponse) -> Unit {
        return { asyncResponse: AsyncResponse ->
            resourceExecutor.submit {
                if (lock.isLocked) {
                    val result = Response(
                            "Hardware busy.",
                            null
                            ,
                            "error")
                    asyncResponse.resume(result)
                    return@submit
                }
                lock.lock()
                try {
                    val result = body()
                    asyncResponse.resume(result)
                }
                finally {
                    lock.unlock()
                }
            }
        }
    }
}