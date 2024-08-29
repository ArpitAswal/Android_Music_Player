package com.example.playmusic.globalclass

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.example.playmusic.R
import com.example.playmusic.views.activities.MusicPlayerActivity

object PlayerNotification {
    private var shuffleMode = false
    private var repeatMode = 1
    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSessionCompat? = null

    @SuppressLint("StaticFieldLeak")
    var playerNotificationManager: PlayerNotificationManager? = null

    fun initializePlayer(context: Context, exoPlayer: ExoPlayer, musicId: Long) {
        PlayerNotification.exoPlayer = exoPlayer
        // Initialize MediaSession
        mediaSession = MediaSessionCompat(context, "GlobalMusicService").apply {
            isActive = true
        }
        // Set up the PlayerNotificationManager
        setupPlayerNotificationManager(context, musicId)
    }

    fun getShuffleValue(): Boolean {
        return shuffleMode
    }

    fun setShuffleValue(mode: Boolean) {
        shuffleMode = mode
    }

    fun getRepeatMode(): Int {
        return repeatMode
    }

    fun setRepeatMode(mode: Int) {
        repeatMode = mode
    }

    @OptIn(UnstableApi::class)
    private fun setupPlayerNotificationManager(context: Context, musicId: Long) {
        val notificationChannelId = musicId.toString()
        val notificationId = 1

        playerNotificationManager = PlayerNotificationManager.Builder(
            context,
            notificationId,
            notificationChannelId
        )
            .setChannelNameResourceId(R.string.channel_name)
            .setChannelDescriptionResourceId(R.string.channel_description)
            .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): CharSequence {
                    return player.currentMediaItem?.mediaMetadata?.title ?: "Unknown Title"
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    val intent = Intent(context, MusicPlayerActivity::class.java)
                        .apply {
                            putExtra("MusicData", musicId.toString())
                            flags =
                                Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                    return PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                override fun getCurrentContentText(player: Player): CharSequence? {
                    return player.currentMediaItem?.mediaMetadata?.albumTitle
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    // Load album art or other large icon here
                    val artworkData = player.currentMediaItem?.mediaMetadata?.artworkData

                    return if (artworkData != null && artworkData.isNotEmpty()) {
                        // If artwork data is available, decode it into a Bitmap
                        BitmapFactory.decodeByteArray(artworkData, 0, artworkData.size)
                    } else {
                        // If artwork data is null or empty, return a placeholder image or null
                        BitmapFactory.decodeResource(context.resources, R.drawable.headphones)
                        // Or return null if no placeholder is desired null
                        //null
                    }
                }

            })
            .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    // Start foreground service or handle notification lifecycle
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    // Handle notification cancellation
                }
            })
            .setSmallIconResourceId(R.drawable.app_logo)
            .build()
        playerNotificationManager?.setPlayer(exoPlayer)
        mediaSession?.sessionToken?.let { playerNotificationManager?.setMediaSessionToken(it) }
    }

    @OptIn(UnstableApi::class)
    fun releasePlayer() {
        playerNotificationManager?.setPlayer(null)
        exoPlayer?.release()
        exoPlayer = null
        mediaSession?.release()
        mediaSession = null
    }
}

