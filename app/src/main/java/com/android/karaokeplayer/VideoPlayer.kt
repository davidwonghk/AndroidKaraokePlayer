package com.android.karaokeplayer

import android.graphics.Color
import android.net.Uri
import android.net.Uri.Builder
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.runBlocking

fun nextUri(host: String, port: Int): Uri {
  val id = System.currentTimeMillis();
  val uri = Builder().apply {
    scheme("http")
    encodedAuthority("$host:$port")
    appendPath("play")
    appendQueryParameter("id", id.toString())
  }.build()
  return uri
}

/**
 * Video player
 */
@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer() {
  val context = LocalContext.current
  val (host, port, wsport) = runBlocking {
     discover("0.0.0.0")
  }

  val renderFactory = DefaultRenderersFactory(context).apply {
    setExtensionRendererMode(EXTENSION_RENDERER_MODE_ON)
  }
  val exoPlayer = remember {
    ExoPlayer.Builder(context, renderFactory)
      .build()
      .apply {
        playWhenReady = true
        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        repeatMode = Player.REPEAT_MODE_OFF

        addListener(object : Player.Listener {
          override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            addMediaItem(MediaItem.fromUri(nextUri(host, port)))
            super.onMediaItemTransition(mediaItem, reason)
          }
        })

        addMediaItem(MediaItem.fromUri(nextUri(host, port)))
        addMediaItem(MediaItem.fromUri(nextUri(host, port)))

        prepare()
        play()
      }
  }

  val client = acceptControl(exoPlayer, host, wsport)

  DisposableEffect(
    AndroidView(factory = {
      PlayerView(context).apply {
        hideController()
        setBackgroundColor(Color.BLACK)
        useController = false
        this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
      }
    })
  ) {
    onDispose {
      exoPlayer.release()
      client.close()
    }
  }
}