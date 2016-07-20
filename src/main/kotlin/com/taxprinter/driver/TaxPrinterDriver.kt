package com.taxprinter.driver

/**
 * This interface should be implemented by all the printer drivers to be
 * used by the application.
 * Created by george on 04/07/16.
 */
import com.taxprinter.models.State
import com.taxprinter.models.Version

interface TaxPrinterDriver {
    fun getVersion(): Version
    fun getState(): State // Gets printer state
    fun printXReport() // Prints X Report



}