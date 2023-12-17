package com.android.karaokeplayer

import android.os.Handler
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.exoplayer.ExoPlayer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

fun acceptControl(exoPlayer: ExoPlayer, wsHost: String, wsPort: Int): HttpClient {
  val handler = Handler(exoPlayer.applicationLooper)
  val client = HttpClient {
    install(WebSockets)
  }
  val scope = CoroutineScope(Dispatchers.IO)
  scope.launch {
    client.webSocket(method = HttpMethod.Get, host = wsHost, port = wsPort, path = "/") {
      while (true) {
        val message = incoming.receive() as? Frame.Text ?: continue
        val json = JSONObject(message.readText())
        if (json.has("skip")) {
          handler.post {
            exoPlayer.seekToNextMediaItem()
          }
        }
        if (json.has("switchAudio")) {
          handler.post {
            val audioTrackGroup = exoPlayer.currentTracks.groups.find{!it.isSelected}
            if (audioTrackGroup != null) {
              exoPlayer.trackSelectionParameters =
                exoPlayer.trackSelectionParameters
                  .buildUpon()
                  .setOverrideForType(
                    TrackSelectionOverride(audioTrackGroup.mediaTrackGroup, 0)
                  ).build()
            }
          }
        }
        //send("done")
      }
    }
  }
  return client;
}

