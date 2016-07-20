package com.taxprinter.boot

import com.hubspot.dropwizard.guice.GuiceBundle
import com.taxprinter.configs.TaxprinterConfig
import com.taxprinter.modules.DriverModule
import com.taxprinter.resources.StateResource
import com.taxprinter.resources.VersionResource
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment



/**
 * Created by george on 03/07/16.
 */
fun main(args: Array<String>) {
    val args = arrayOf("server", "taxprinter.yml")
    TaxPrinterApplication().run(*args)
}

class TaxPrinterApplication() : Application<TaxprinterConfig>() {

    override fun run(configuration: TaxprinterConfig?,
                     environment: Environment?) {
        // as these resources has injected dependencies we register
        // them using java class references (which are not the same
        // than kotlin class references
        environment?.jersey()?.register(VersionResource::class.java)
        environment?.jersey()?.register(StateResource::class.java)
    }

    override fun getName(): String {
        return "tax-printer"
    }

    override fun initialize(bootstrap: Bootstrap<TaxprinterConfig>?) {
        val guiceBundle = GuiceBundle.newBuilder<TaxprinterConfig>()
        .addModule(DriverModule())
        .setConfigClass(TaxprinterConfig::class.java)
        .enableAutoConfig(javaClass.`package`.name)
        .build()

        bootstrap?.addBundle(guiceBundle)
    }

}