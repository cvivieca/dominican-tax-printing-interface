package com.taxprinter.models

import org.hibernate.validator.constraints.NotEmpty

/**
 * Created by george on 03/07/16.
 */

/**
 * Class which represents a FiscalStatus instance
 *
 * @param document: Processing document on the printer
 * @param memory: Fiscal Memory status
 * @param mode: Hardware working mode
 * @param substate: Misc substates
 */
class FiscalStatus(
        @NotEmpty val document: String,
        @NotEmpty val memory: String,
        @NotEmpty val mode: String,
        @NotEmpty val substate: String,
        @NotEmpty val techmode: Boolean,
        @NotEmpty val open: Boolean
)