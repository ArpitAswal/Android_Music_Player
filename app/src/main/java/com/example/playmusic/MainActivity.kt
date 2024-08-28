package com.example.playmusic

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playmusic.dataobject.MusicData
import com.example.playmusic.globalclass.DeviceMusic
import com.example.playmusic.globalclass.ExoPlayerSingleton
import com.example.playmusic.globalclass.PlayerNotification


class MainActivity : AppCompatActivity() {
    private lateinit var myRecView: RecyclerView
    private lateinit var myAdapter: MainViewAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var exoPlayer: ExoPlayer
    private var mediaPlayer: MediaPlayer? = null
    private var currentlyPlayingPosition: Int = -1

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRecView = findViewById(R.id.recView)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        exoPlayer = ExoPlayerSingleton.getInstance(this)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        requestPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        PlayerNotification.releasePlayer()
    }

    // method to inflate the options menu when the user opens the menu for the first time
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissions() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, permission) -> {
                // Permission granted, load the music files
                DeviceMusic.loadMusicFiles(this@MainActivity)
                setupRecyclerView()
            }

            else -> {
                // Request permission
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            DeviceMusic.loadMusicFiles(this@MainActivity)
            setupRecyclerView()
        } else {
            // Show a message to the user that permission is required
            Toast.makeText(
                this@MainActivity,
                "Permissions are necessary to display list of songs",
                Toast.LENGTH_SHORT
            ).show()
            Handler(Looper.getMainLooper()).postDelayed({
                requestPermissions()
            }, 3000)
        }
    }


    private fun setupRecyclerView() {
        val musicList = DeviceMusic.musicList

        // create a vertical layout manager
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // Set up RecyclerView with the adapter
        myAdapter = MainViewAdapter(
            this@MainActivity,
            musicList,
            mediaPlayer,
            currentlyPlayingPosition,
            exoPlayer
        )
        myRecView.adapter = myAdapter
        myRecView.layoutManager = layoutManager
        // Add custom divider
        val dividerItemDecoration = DividerItemDecoration(
            myRecView.context,
            layoutManager.orientation
        )
        ContextCompat.getDrawable(this, R.drawable.custom_divider)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        myRecView.addItemDecoration(dividerItemDecoration)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortMusicList(musicList: List<MusicData>, order: SortOrder) {
        when (order) {
            SortOrder.TITLE_ASC -> musicList.sortedBy { it.title }
            SortOrder.TITLE_DESC -> musicList.sortedByDescending { it.title }
            SortOrder.DATE_ASC -> musicList.sortedBy { it.duration } // Assuming date is stored in duration
            SortOrder.DATE_DESC -> musicList.sortedByDescending { it.duration }
        }
        myAdapter.notifyDataSetChanged()
    }
}

enum class SortOrder {
    TITLE_ASC,
    TITLE_DESC,
    DATE_ASC,
    DATE_DESC
}
