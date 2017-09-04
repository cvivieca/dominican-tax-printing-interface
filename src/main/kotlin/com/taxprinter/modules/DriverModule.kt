package com.taxprinter.modules

import com.google.inject.AbstractModule
import com.google.inject.Provider
import com.google.inject.Provides
import com.google.inject.name.Named
import com.taxprinter.configs.TaxprinterConfig
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.driver.bixolonsrp350.Srp350Driver
import com.taxprinter.driver.bixolonsrp350.utils.Client
import com.taxprinter.driver.bixolonsrp350.utils.SerialClient
import org.apache.log4j.Logger
import kotlin.system.exitProcess

/**
 * This class is a Guice module to inject the proper printer driver
 * Created by george on 04/07/16.
 */

class DriverModule(): AbstractModule() {
    companion object {
        val logger: Logger = Logger.getLogger(DriverModule::class.java)
    }
    // Reusable provider for runtime dependency injection
    var clientProvider: Provider<Client>? = null

    override fun configure() {
        /**
         * Type-safe direct bindings: Ex: If Interface is found then inject
         * an object of this class.
         */
        //bind(TaxPrinterDriver::class.java).to(Srp350Driver::class.java)
        bind(Client::class.java).to(SerialClient::class.java)
        clientProvider = this.getProvider(Client::class.java)

    }

    /**
     * This function is used by the DI to inject the correct port
     * descriptor to the low level serial port client
     * @param config: passed by Guice bundle, its the server config
     * @return String: the port descriptor
     */
    @Provides
    @Named("portDescriptor")
    fun providePortDescriptor(config: TaxprinterConfig): String {
        return config.portDescriptor ?: "COM1"
    }

    /**
     * This function is used by the DI to inject the current printer mode
     * if it is on fast food mode
     * TODO: move this to read the mode from the printer
     */
    @Provides
    @Named("isFastFoodMode")
    fun provideIsFastFoodMode(config: TaxprinterConfig): Boolean {
        return config.isFastFoodMode
    }

    /**
     * This function is used by the DI to inject the proper driver
     * on runtime depending on config value
     * @param config : passed by Guice bundle, its the server config
     * @return [TaxPrinterDriver] a proper driver object implementation
     */
    @Provides
    fun provideDriver(config: TaxprinterConfig): TaxPrinterDriver {
        val client = clientProvider!!.get() // Unsafe call over null
        when (config.driver) {
            "bixolonsrp350" -> return Srp350Driver(client)
            else -> {
                logger.fatal("Driver module not found!")
                exitProcess(1)
            }
        }
    }

}