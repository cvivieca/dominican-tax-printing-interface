package com.taxprinter.boot

import com.taxprinter.configs.TaxprinterConfig
import com.taxprinter.resources.StateResource
import com.taxprinter.resources.VersionResource
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

/**
 * Created by george on 03/07/16.
 */
fun main(args: Array<String>) {
    val args = arrayOf("server")
    TaxPrinterApplication().run(*args)
}

class TaxPrinterApplication() : Application<TaxprinterConfig>() {

    override fun run(configuration: TaxprinterConfig?,
                     environment: Environment?) {
        environment?.jersey()?.register(VersionResource())
        environment?.jersey()?.register(StateResource())
    }

    override fun getName(): String {
        return "tax-printer"
    }

    override fun initialize(bootstrap: Bootstrap<TaxprinterConfig>?) {
        // Nothing to do yet
    }

}