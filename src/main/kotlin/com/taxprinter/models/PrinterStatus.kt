package com.taxprinter.models

import com.taxprinter.models.enums.*
import org.hibernate.validator.constraints.NotEmpty

/**
 * Created by george on 03/07/16.
 */

/**
 * Class representing the Printer hardware status
 *
 * @param cover: Status of the printer cover
 * @param state: Printer state
 * @param errors: If any errors on the printer
 * @param moneybox: Is the moneybox open or what?
 * @param printer: Kind of printer
 */
class PrinterStatus(
        @NotEmpty val cover: Cover,
        @NotEmpty val state: PrinterState,
        @NotEmpty val errors: Errors,
        @NotEmpty val moneybox: Moneybox,

        @NotEmpty
        val printer: PrinterKind
)