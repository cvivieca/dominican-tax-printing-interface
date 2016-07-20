package com.taxprinter.models

import com.taxprinter.models.enums.DocumentType
import com.taxprinter.models.enums.SubState
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
        @NotEmpty val document: DocumentType,
        @NotEmpty val memory: String,
        @NotEmpty val mode: String,
        @NotEmpty val substate: SubState,
        @NotEmpty val techmode: Boolean,
        @NotEmpty val open: Boolean
)