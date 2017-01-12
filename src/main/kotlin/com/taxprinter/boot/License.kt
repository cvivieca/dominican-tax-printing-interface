package com.taxprinter.boot

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by george on 31/07/16.
 */
class License {
    companion object {
        fun check(): Boolean {
            var dt = "2017-02-01"  // Start date
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val c = Calendar.getInstance()
            c.time = sdf.parse(dt)
            c.add(Calendar.DATE, 30)  // number of days to add
            val nowd = Calendar.getInstance()
            return nowd.time <= c.time
        }
    }
}