package com.example.playmusic.views.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playmusic.R
import com.example.playmusic.adapter.AllSongsViewAdapter
import com.example.playmusic.dataobject.DBMusicData
import com.example.playmusic.globalclass.DeviceMusic
import com.example.playmusic.globalclass.ExoPlayerSingleton
import com.example.playmusic.globalclass.PlayerNotification

class AllSongsFragment : Fragment() {

    private lateinit var myRecView: RecyclerView
    private lateinit var myAdapter: AllSongsViewAdapter
    private lateinit var exoPlayer: ExoPlayer
    private var musicList: MutableList<DBMusicData> = mutableListOf()
    private var mediaPlayer: MediaPlayer? = null
    private var currentlyPlayingPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_songs, container, false)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissions() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        when (PackageManager.PERMISSION_GRANTED) {
            view?.let { ContextCompat.checkSelfPermission(it.context, permission) } -> {
                // Permission granted, load the music files
                DeviceMusic.loadMusicFiles(requireView().context)
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
            view?.let { DeviceMusic.loadMusicFiles(it.context) }
            setupRecyclerView()
        } else {
            // Show a message to the user that permission is required
            Toast.makeText(
                view?.context,
                "Permissions is necessary to display list of songs",
                Toast.LENGTH_SHORT
            ).show()
            Handler(Looper.getMainLooper()).postDelayed({
                requestPermissions()
            }, 3000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myRecView = view.findViewById(R.id.all_songs_recView)
        exoPlayer = ExoPlayerSingleton.getInstance(view.context)
    }

    private fun setupRecyclerView() {
        musicList = DeviceMusic.musicList

        // create a vertical layout manager
        val layoutManager =
            LinearLayoutManager(requireView().context, LinearLayoutManager.VERTICAL, false)
        // Set up RecyclerView with the adapter
        myAdapter = AllSongsViewAdapter(
            musicList,
            mediaPlayer,
            currentlyPlayingPosition,
            requireView().context,
        )
        myRecView.adapter = myAdapter
        myRecView.layoutManager = layoutManager
        // Add custom divider
        val dividerItemDecoration = DividerItemDecoration(
            myRecView.context,
            layoutManager.orientation
        )
        ContextCompat.getDrawable(requireView().context, R.drawable.custom_divider)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        myRecView.addItemDecoration(dividerItemDecoration)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(text: String, b: Boolean) {
        var customList = musicList.toMutableList()
        if (customList.isEmpty()) {
            view?.let {
                Toast.makeText(it.context, "Music list not loaded yet.", Toast.LENGTH_SHORT).show()
            }
            return
        }

        customList = customList.filter {
            it.title.contains(text, ignoreCase = true)
        }.toMutableList()

        myAdapter.updateData(customList)

        if (b && customList.isEmpty()) {
            view?.let {
                Toast.makeText(it.context, "No Data Found..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortMusicList(musicList: List<DBMusicData>, order: SortOrder) {
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
