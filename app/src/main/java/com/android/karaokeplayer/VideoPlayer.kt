package com.android.karaokeplayer

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

fun nextUri(base: String): String {
  val id = System.currentTimeMillis();
  return "$base?id=$id";
}

/**
 * Video player
 *
 * @param uri
 */
@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(uri: String) {
  val context = LocalContext.current
  val exoPlayer = remember {
    ExoPlayer.Builder(context)
      .build()
      .apply {
        playWhenReady = true
        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        repeatMode = Player.REPEAT_MODE_OFF

        addListener(object: Player.Listener {
          override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            addMediaItem(MediaItem.fromUri(nextUri(uri)))
            super.onMediaItemTransition(mediaItem, reason)
          }
        })

        addMediaItem(MediaItem.fromUri(nextUri(uri)))
        addMediaItem(MediaItem.fromUri(nextUri(uri)))

        prepare()
        play()
      }
  }

  val client = acceptControl(exoPlayer)

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
      client.close()
    }
  }
}