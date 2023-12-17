package com.android.karaokeplayer

import android.net.Uri
import android.net.Uri.Builder
import android.os.Handler
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

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
 *
 * @param uri
 */
@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer() {
  val context = LocalContext.current
  val exoPlayer = remember {
    ExoPlayer.Builder(context)
      .build()
      .apply {
        playWhenReady = true
        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        repeatMode = Player.REPEAT_MODE_OFF
      }
  }

  discover { host, port, wsport ->
    acceptControl(exoPlayer, host, wsport)
    val handler = Handler(exoPlayer.applicationLooper)
    handler.run {
      exoPlayer.apply {
        addListener(object : Player.Listener {
          override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            println("david: onMediaItemTransition")
            addMediaItem(MediaItem.fromUri(nextUri(host, port)))
            super.onMediaItemTransition(mediaItem, reason)
          }
        })

        println("david: discover callback 1")
        addMediaItem(MediaItem.fromUri(nextUri(host, port)))
        println("david: discover callback 2")
        addMediaItem(MediaItem.fromUri(nextUri(host, port)))
        println("david: discover callback 3")

        prepare()
        play()
      }

    }
  }

  DisposableEffect(
    AndroidView(factory = {
      PlayerView(context).apply {
        //hideController()
        useController = true
        this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
      }
    })
  ) {
    onDispose {
      exoPlayer.release()
      //client.close()
    }
  }
}