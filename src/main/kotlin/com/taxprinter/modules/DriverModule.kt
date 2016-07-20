package com.taxprinter.modules

import com.google.inject.AbstractModule
import com.google.inject.Binder
import com.google.inject.Module
import com.google.inject.Provides
import com.google.inject.name.Named
import com.taxprinter.configs.TaxprinterConfig
import com.taxprinter.driver.TaxPrinterDriver
import com.taxprinter.driver.bixolonsrp350.Srp350Driver
import com.taxprinter.driver.bixolonsrp350.utils.Client
import com.taxprinter.driver.bixolonsrp350.utils.SerialClient

/**
 * This class is a Guice module to inject the proper printer driver
 * Created by george on 04/07/16.
 */

class DriverModule(): AbstractModule() {
    override fun configure() {
        /**
         * Type-safe direct bindings: Ex: If Interface is found then inject
         * an object of this class. We should parameterize this in order to
         * load a class depending on a config file (change printer driver)
         */
        bind(TaxPrinterDriver::class.java).to(Srp350Driver::class.java)
        bind(Client::class.java).to(SerialClient::class.java)

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

}