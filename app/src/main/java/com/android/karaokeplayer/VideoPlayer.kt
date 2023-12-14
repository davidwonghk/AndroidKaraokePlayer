package com.android.karaokeplayer

import android.net.Uri
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

class VideoPathGenerator {
  companion object{
    var id = 0

    fun  next(): Uri {
      id += 1
      return Uri.parse("http://192.168.8.124:8080/play?id=$id")
    }
  }
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
        /*
        val defaultDataSourceFactory = DefaultDataSource.Factory(context)
        val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
          context,
          defaultDataSourceFactory
        )
        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
          .createMediaSource(MediaItem.fromUri(uri))
        setMediaSource(source)
         */
        playWhenReady = true
        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        repeatMode = Player.REPEAT_MODE_OFF
        addListener(object : Player.Listener {
          override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
              Player.STATE_IDLE -> {}
              Player.STATE_BUFFERING -> {}
              Player.STATE_READY -> {}
              Player.STATE_ENDED -> {
                addMediaItem(MediaItem.fromUri(VideoPathGenerator.next()));
              }
            }
          }
        })
        addMediaItem(MediaItem.fromUri(VideoPathGenerator.next()))
        prepare()
      }
  }

  DisposableEffect(
    AndroidView(factory = {
      PlayerView(context).apply {
        //hideController()
        useController = true
        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
      }
    })
  ) {
    onDispose { exoPlayer.release() }
  }
}