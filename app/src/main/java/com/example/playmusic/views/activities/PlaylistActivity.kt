package com.example.playmusic.views.activities

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playmusic.R
import com.example.playmusic.adapter.AddPlaylistViewAdapter
import com.example.playmusic.adapter.AllSongsViewAdapter
import com.example.playmusic.dataobject.MusicData
import com.example.playmusic.dataobject.PlaylistData
import com.example.playmusic.globalclass.AllPlaylistExist
import com.example.playmusic.globalclass.DeviceMusic


class PlaylistActivity : AppCompatActivity() {
    private lateinit var backBtn: ImageButton
    private lateinit var newPlaylist: Button
    private lateinit var doneBtn: Button
    private lateinit var myRecView: RecyclerView
    private lateinit var myAdapter: AddPlaylistViewAdapter
    private lateinit var playlistList: MutableList<PlaylistData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        myRecView = findViewById(R.id.create_playlist_recView)
        backBtn = findViewById(R.id.playlist_backBtn)
        newPlaylist = findViewById(R.id.newlist_BtnView)
        doneBtn = findViewById(R.id.done_BtnView)

        val music = intent.getParcelableExtra<MusicData>("SelectMusic")
        val drawable = newPlaylist.background as GradientDrawable
        drawable.setColor(resources.getColor(R.color.white, null))
        // Set the border (stroke) color dynamically
        drawable.setStroke(2, resources.getColor(R.color.black))

        backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
        }

        doneBtn.setOnClickListener {
            AllPlaylistExist.songAddTo = music!!
            AllPlaylistExist.addSongsInLists()
            finish()
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
        }

        playlistList = AllPlaylistExist.getAllPlaylists()
        // create a vertical layout manager
        val layoutManager =
            LinearLayoutManager(this@PlaylistActivity, LinearLayoutManager.VERTICAL, false)
        // Set up RecyclerView with the adapter
        myAdapter = AddPlaylistViewAdapter(
            playlistList,
            this@PlaylistActivity,
        )
        myRecView.adapter = myAdapter
        myRecView.layoutManager = layoutManager
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
    }


}