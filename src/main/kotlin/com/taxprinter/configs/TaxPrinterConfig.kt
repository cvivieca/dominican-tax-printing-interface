package com.taxprinter.configs

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import org.hibernate.validator.constraints.NotEmpty

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
}