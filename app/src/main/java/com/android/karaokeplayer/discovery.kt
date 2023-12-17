package com.android.karaokeplayer

import android.net.Uri
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun discover(hostName: String): Triple<String, Int, Int> {
  return suspendCoroutine {cont ->
    // Create a JmDNS instance
    val jmdns = JmDNS.create(InetAddress.getByName(hostName), "karaokeMdns")

    // Add a service listener
    jmdns.addServiceListener("_http._tcp.local.", object : ServiceListener {
      override fun serviceResolved(event: ServiceEvent) {
        if (!event.info.hasData()) return
        if ("karaoke" != event.info.getPropertyString("app")) return

        val urls = event.info.getURLs()
        if (urls.size == 0) return

        val uri = Uri.parse(urls.get(0))
        val host = uri.host ?: return
        if (!host.startsWith("192.168")) return

        jmdns.close()
        val port = event.info.getPropertyString("port").toInt()
        val wsPort = event.info.getPropertyString("wsport").toInt()
        cont.resume(Triple(host, port, wsPort))
      }

      override fun serviceAdded(event: ServiceEvent?) {
      }

      override fun serviceRemoved(event: ServiceEvent?) {
      }
    })

  }
}
