package com.android.karaokeplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.android.karaokeplayer.ui.theme.AndroidKaraokePlayerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidKaraokePlayerTheme {
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RectangleShape
                ) {
                  discover()
                  VideoPlayer("http://192.168.8.124:8080/play")
                }
            }
        }
    }


}

