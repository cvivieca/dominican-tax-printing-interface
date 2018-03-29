package com.taxprinter.boot

import com.taxprinter.models.Hardware
import oshi.SystemInfo
import java.text.SimpleDateFormat
import java.util.*
import oshi.hardware.HardwareAbstractionLayer



/**
 * Created by george on 31/07/16.
 */
class License {
    companion object {

        val hal = SystemInfo().hardware

        fun uniqueId(): Hardware {
            val cs = hal.computerSystem
            val cpu = hal.processor
            val boardSerial = cs.baseboard.serialNumber
            val equipmentSerial = cs.serialNumber
            val cpuId = cpu.processorID

            val allTogether = boardSerial + equipmentSerial + cpuId
            val uniqueId = org.apache.commons.codec.digest.DigestUtils.sha256Hex(allTogether)

            return Hardware(uniqueId, boardSerial, equipmentSerial)

        }
        fun check(): Boolean {
            var dt = "2017-06-01"  // Start date
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val c = Calendar.getInstance()
            c.time = sdf.parse(dt)
            c.add(Calendar.DATE, 365)  // number of days to add
            val nowd = Calendar.getInstance()
            return nowd.time <= c.time
        }
    }
}