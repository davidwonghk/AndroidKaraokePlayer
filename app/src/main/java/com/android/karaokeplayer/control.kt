package com.android.karaokeplayer

import androidx.media3.exoplayer.ExoPlayer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun acceptControl(exoPlayer: ExoPlayer) {
  println("david: acceptControl")
  val client = HttpClient {
    install(WebSockets)
  }
  val scope = CoroutineScope(Dispatchers.IO)
  scope.launch {
    client.webSocket(method = HttpMethod.Get, host = "192.168.8.124", port = 8082, path = "/") {
      while (true) {
        println("david: client is running")
        val message = incoming.receive() as? Frame.Text
        val messageTxt = message?.readText()
        println("david: " + messageTxt)
        send("done")
      }
    }
  }

  client.close()
}

