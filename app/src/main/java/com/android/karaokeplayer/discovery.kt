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
    val jmdns = JmDNS.create(InetAddress.getLocalHost())

    // Add a service listener
    jmdns.addServiceListener("_http._tcp.local.", SampleListener())

  } catch (e: UnknownHostException) {
    println("david: " + e.message)
  } catch (e: IOException) {
    println("david: " + e.message)
  }
}

private class SampleListener : ServiceListener {
  override fun serviceAdded(event: ServiceEvent) {
    println("david Service added: " + event.info)
  }

  override fun serviceRemoved(event: ServiceEvent) {
    println("david Service removed: " + event.info)
  }

  override fun serviceResolved(event: ServiceEvent) {
    println("david Service resolved: " + event.info)
  }
}
