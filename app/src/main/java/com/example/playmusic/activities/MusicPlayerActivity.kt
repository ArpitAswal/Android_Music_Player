package com.example.playmusic.activities

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.playmusic.R
import com.example.playmusic.dataobject.MusicData
import com.example.playmusic.globalclass.DeviceMusic
import com.example.playmusic.globalclass.ExoPlayerSingleton
import com.example.playmusic.globalclass.PlayerNotification

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var back: ImageButton
    private lateinit var shuffleBtn: ImageButton
    private lateinit var repeatBtn: ImageButton
    private lateinit var title: TextView
    private lateinit var artistName: TextView
    private lateinit var albumName: TextView
    private lateinit var name: String
    private var musicList: List<MusicData> = listOf()
    private var lastMusicListVersion: Int = 0
    private var seekIndex: Int = 0
    private var savedPlaybackPosition: Long = 0
    private var currentMusic: MusicData? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        currentMusic = intent.getParcelableExtra("MusicData")
        name = ""
        // Initialize PlayerView from layout
        playerView = findViewById(R.id.player_view)
        back = findViewById(R.id.backBtn)
        title = findViewById(R.id.titleView)
        artistName = findViewById(R.id.ArtistName)
        albumName = findViewById(R.id.AlbumName)
        shuffleBtn = findViewById(R.id.shuffle_BtnView)
        repeatBtn = findViewById(R.id.repeat_BtnView)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayerSingleton.getInstance(this)
        playerView.player = exoPlayer

        musicList = DeviceMusic.musicList
        seekIndex = musicList.indexOf(currentMusic)

        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        shuffleBtn.setOnClickListener {
            val mode = !PlayerNotification.getShuffleValue()
            exoPlayer.shuffleModeEnabled = mode
            PlayerNotification.setShuffleValue(mode)
            Toast.makeText(
                this@MusicPlayerActivity,
                "shuffleMode: ${if (mode) "ON" else "OFF"}",
                Toast.LENGTH_SHORT
            ).show()
        }

        repeatBtn.setOnClickListener {
            when (PlayerNotification.getRepeatMode()) {
                1 -> PlayerNotification.setRepeatMode(2)
                2 -> PlayerNotification.setRepeatMode(3)
                else -> PlayerNotification.setRepeatMode(1)
            }
            exoPlayer.repeatMode =
                if (PlayerNotification.getRepeatMode() == 1) Player.REPEAT_MODE_OFF else if (PlayerNotification.getRepeatMode() == 2) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_ALL
            if (PlayerNotification.getRepeatMode() == 2)
                Toast.makeText(
                    this@MusicPlayerActivity,
                    "current song repeat in an endless loop",
                    Toast.LENGTH_SHORT
                ).show()
            else if (PlayerNotification.getRepeatMode() == 3)
                Toast.makeText(
                    this@MusicPlayerActivity,
                    "all playlist repeat in an endless loop",
                    Toast.LENGTH_SHORT
                ).show()
        }

        if (currentMusic != null) {
            name = getArtistName(currentMusic!!.title)
            albumName.text = "Album: ${currentMusic!!.album}"
            artistName.text = name
        }

        lastMusicListVersion = DeviceMusic.musicList.hashCode()

    }

    private fun handlePlayback() {
        val isSameSong = exoPlayer.currentMediaItem?.mediaId == currentMusic?.id.toString()

        if (!isSameSong) {
            // Stop the previous song if it's different
            exoPlayer.stop()
            preparePlaylist(musicList, currentMusic, "handlePlay")
        } else {
            // Resume playback if the same song is selected
            val duration = ExoPlayerSingleton.getExoSongLastDuration()
            exoPlayer.seekTo(duration)
            if (exoPlayer.isPlaying)
                exoPlayer.playWhenReady = true

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onStart() {
        super.onStart()
        // Refresh the playlist if the music list has been updated
        // val previousHashCode = DeviceMusic.musicList.hashCode()
        if (exoPlayer != null && exoPlayer.isPlaying) {
            savedPlaybackPosition = exoPlayer.currentPosition
        }
        DeviceMusic.loadMusicFiles(this)
        if (DeviceMusic.musicList.hashCode() != lastMusicListVersion) {
            updatePlaylist()
        } else {
            handlePlayback()
        }
        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                // Called when the media item changes (next/previous or manually)
                val fadeOut = AnimationUtils.loadAnimation(playerView.context, R.anim.fade_out)
                playerView.startAnimation(fadeOut)

                exoPlayer.shuffleModeEnabled = PlayerNotification.getShuffleValue()
                exoPlayer.repeatMode =
                    if (PlayerNotification.getRepeatMode() == 1) Player.REPEAT_MODE_OFF else if (PlayerNotification.getRepeatMode() == 2) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_ALL
                val currentIndex = exoPlayer.currentMediaItemIndex
                if (currentIndex != 0)
                    seekIndex = currentIndex
                PlayerNotification.initializePlayer(
                    this@MusicPlayerActivity,
                    exoPlayer,
                    musicList[currentIndex].id
                )
                name = getArtistName(musicList[currentIndex].title)
                albumName.text =
                    "Album: ${exoPlayer.currentMediaItem?.mediaMetadata?.albumTitle ?: "Unknown"}"
                artistName.text = name
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                val cause = error.cause
                if (cause is HttpDataSource.HttpDataSourceException) {
                    // An HTTP error occurred.
                    // It's possible to find out more about the error both by casting and by querying the cause.
                    Toast.makeText(
                        this@MusicPlayerActivity,
                        cause.message.toString(), Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

    }

    private fun updatePlaylist() {
        // Update the music list and refresh ExoPlayer's playlist
        musicList = DeviceMusic.musicList
        lastMusicListVersion = musicList.hashCode()
        preparePlaylist(musicList, currentMusic, "updatePlaylist")
    }

    private fun getArtistName(artists: String): String {
        val artist = artists.split("|")
        name = ""
        title.text = artist[0]
        var i = 1
        while (i < artist.size) {
            name += artist[i]
            i++
        }
        name.trim()
        return if (name.isEmpty())
            "Artist: Unknown"
        else
            "Artist: $name"
    }

    @OptIn(UnstableApi::class)
    @SuppressLint("SetTextI18n")
    private fun preparePlaylist(
        musicList: List<MusicData>,
        currentMusic: MusicData?,
        listBehaviour: String
    ) {
        exoPlayer.clearMediaItems()
        for ((index, music) in musicList.withIndex()) {
            name = getArtistName(musicList[index].title)
            val mediaItem = MediaItem.Builder()
                .setUri(music.musicUri) // Set the URI of the media
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(music.title)
                        .setAlbumTitle(music.album)
                        .setArtworkData(getAlbumCover(music.path))
                        .setArtist(name)
                        .build()
                ).setMediaId(currentMusic?.id.toString())
                .build()
            exoPlayer.addMediaItem(mediaItem)

        }
        exoPlayer.prepare() // Prepare the player to start the playlist
        exoPlayer.shuffleModeEnabled = PlayerNotification.getShuffleValue()
        exoPlayer.repeatMode =
            if (PlayerNotification.getRepeatMode() == 1) Player.REPEAT_MODE_OFF else if (PlayerNotification.getRepeatMode() == 2) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_ALL
        if (listBehaviour == "handlePlay") {
            exoPlayer.seekTo(seekIndex, 0)
        } else {
            exoPlayer.seekTo(seekIndex, savedPlaybackPosition)
        }
        exoPlayer.playWhenReady = true
        refreshPlayerNotification()
    }

    @OptIn(UnstableApi::class)
    private fun refreshPlayerNotification() {
        PlayerNotification.playerNotificationManager?.setPlayer(null)  // Temporarily remove the player
        PlayerNotification.playerNotificationManager?.setPlayer(exoPlayer)  // Reassign the player
        PlayerNotification.initializePlayer(this@MusicPlayerActivity, exoPlayer, currentMusic!!.id)
    }

    private fun getAlbumCover(path: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }
}