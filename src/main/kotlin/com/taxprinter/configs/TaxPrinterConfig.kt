package com.taxprinter.configs

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

/**
 * Created by george on 03/07/16.
 *
 * This source file holds the Main Dropwizard configs
 */

/**
 *  The main configuration for the Taxprinter App
 *
 *  Objects of this class will hold properties from its YAML configs file
 */
class TaxprinterConfig() : Configuration() {

    /**
     * JMS implementation URI
     */
    @JsonProperty
    var jmsURI: String? = null

    /**
     * Port descriptor for the printer
     */
    @JsonProperty
    var portDescriptor: String? = null

    /**
     * Driver implementation to load
     */
    @JsonProperty
    var driver: String? = null

    var swaggerBundleConfiguration: SwaggerBundleConfiguration = { ->
        val bundle = SwaggerBundleConfiguration()
        bundle.resourcePackage = "com.taxprinter.resources"
        bundle
    }()
}