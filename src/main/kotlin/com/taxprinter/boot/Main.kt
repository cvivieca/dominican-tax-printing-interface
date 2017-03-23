package com.taxprinter.boot

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.hubspot.dropwizard.guice.GuiceBundle
import com.taxprinter.configs.TaxprinterConfig
import com.taxprinter.modules.DriverModule
import com.taxprinter.resources.*
import com.taxprinter.tui.Config
import io.dropwizard.Application
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.federecio.dropwizard.swagger.SwaggerBundle
import org.eclipse.jetty.servlets.CrossOriginFilter
import java.util.*
import javax.servlet.DispatcherType
import kotlin.system.exitProcess
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration


/**
 * Created by george on 03/07/16.
 */
fun main(args: Array<String>) {
    if (!License.check()) {
        println("Beta trial expired, ask your provider for a new software build.")
        exitProcess(1)
    }
    if (args.size > 0 && args[0] == "-config") {
        Config(DefaultTerminalFactory().createTerminal())
    }
    val extra = arrayOf("server", "taxprinter.yml")
    TaxPrinterApplication().run(*args+extra)
}

class TaxPrinterApplication() : Application<TaxprinterConfig>() {

    override fun run(configuration: TaxprinterConfig?,
                     environment: Environment?) {
        // as these resources has injected dependencies we register
        // them using java class references (which are not the same
        // than kotlin class references
        environment?.jersey()?.register(VersionResource::class.java)
        environment?.jersey()?.register(StateResource::class.java)
        environment?.jersey()?.register(PrinterInformationResource::class.java)
        environment?.jersey()?.register(FeedPaperResource::class.java)
        environment?.jersey()?.register(ZCloseResource::class.java)
        environment?.jersey()?.register(XReportResource::class.java)
        environment?.jersey()?.register(InvoiceResource::class.java)
        environment?.jersey()?.register(DailyBookResource::class.java)
        environment?.jersey()?.register(JsonProcessingExceptionMapper(true)) // TODO: Exception on response DISABLE on production

        // Avahi service added to dropwizard lifecycle
        environment?.lifecycle()?.manage(AvahiService())

        val filter = environment?.servlets()?.addFilter("CORSFilter", CrossOriginFilter::class.java)

        filter?.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, environment?.applicationContext?.contextPath + "*")
        filter?.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,OPTIONS")
        filter?.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*")
        filter?.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Origin, Content-Type, Accept")
        filter?.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true")

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
        bootstrap?.addBundle(object : SwaggerBundle<TaxprinterConfig>() {
            override fun getSwaggerBundleConfiguration(configuration: TaxprinterConfig): SwaggerBundleConfiguration {
                return configuration.swaggerBundleConfiguration
            }
        })
        bootstrap?.objectMapper?.registerModule(Jdk8Module()) // Support new Optional Jdk8 data type on Jackson POJOs
        bootstrap?.objectMapper?.registerModule(JodaModule()) // Support for Joda time
    }

}