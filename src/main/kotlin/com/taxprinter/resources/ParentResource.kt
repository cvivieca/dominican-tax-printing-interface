package com.taxprinter.resources

import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock

/**
 * ParentResource is a class to manage locks to the printer hardware
 * Created by george on 07/07/16.
 */
open class ParentResource {
    companion object {
        val resourceExecutor = Executors.newFixedThreadPool(Thread.activeCount())
        val lock = ReentrantLock()
    }
}