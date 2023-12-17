package com.android.karaokeplayer

import java.io.IOException
import java.net.InetAddress
import java.net.UnknownHostException
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener


fun discover() {
  try {
    // Create a JmDNS instance
    val jmdns = JmDNS.create(InetAddress.getByName("0.0.0.0"), "karaokeMdns")

    // Add a service listener
    jmdns.addServiceListener("_http._tcp.local.", object: ServiceListener {
      override fun serviceAdded(event: ServiceEvent) {
        println("david Service added: " + event.info)
      }

      override fun serviceRemoved(event: ServiceEvent) {
        println("david Service removed: " + event.info)
      }

      override fun serviceResolved(event: ServiceEvent) {
        println("david Service resolved: " + event.info)
      }
    })

  } catch (e: UnknownHostException) {
    println(e.message)
  } catch (e: IOException) {
    println(e.message)
  }
}
