package com.android.karaokeplayer

import android.net.Uri
import java.io.IOException
import java.net.InetAddress
import java.net.UnknownHostException
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener


fun discover(callback: (String, Int, Int)->Unit) {
  try {
    // Create a JmDNS instance
    val jmdns = JmDNS.create(InetAddress.getByName("0.0.0.0"), "karaokeMdns")

    // Add a service listener
    jmdns.addServiceListener("_http._tcp.local.", object: ServiceListener {
      var resolved = false
      override fun serviceResolved(event: ServiceEvent) {
        if (resolved) return
        if (!event.info.hasData()) return
        if ("karaoke"!=event.info.getPropertyString("app")) return

        val urls = event.info.getURLs()
        if (urls.size == 0) return

        val uri = Uri.parse(urls.get(0))
        val host = uri.host ?: return
        if (!host.startsWith("192.168")) return
        resolved = true
        callback(host, uri.port, event.info.getPropertyString("wsport").toInt())
      }
      override fun serviceAdded(event: ServiceEvent?) {
      }

      override fun serviceRemoved(event: ServiceEvent?) {
      }
    })

  } catch (e: UnknownHostException) {
    println(e.message)
  } catch (e: IOException) {
    println(e.message)
  }
}
