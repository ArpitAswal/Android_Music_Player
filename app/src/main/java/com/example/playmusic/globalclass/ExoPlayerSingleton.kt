package com.example.playmusic.globalclass

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

object ExoPlayerSingleton {
    private var exoPlayer: ExoPlayer? = null

    fun getInstance(context: Context): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        return exoPlayer!!
    }

    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

}
