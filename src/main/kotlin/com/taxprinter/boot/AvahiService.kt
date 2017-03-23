package com.taxprinter.boot

import io.dropwizard.lifecycle.Managed
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

/**
 * Created by george on 22/03/17.
 */
class AvahiService : Managed {
    val jmdns = JmDNS.create(InetAddress.getLocalHost())

    @Throws(Exception::class)
    override fun start() {
        // Register a service
        val serviceInfo: ServiceInfo = ServiceInfo.create("_http._tcp.", "tax-printer-connector", 8080, "model=bixolonsrp350")
        jmdns.registerService(serviceInfo)

    }

    @Throws(Exception::class)
    override fun stop() {
        // Unregister all services
        jmdns.unregisterAllServices()
    }
}