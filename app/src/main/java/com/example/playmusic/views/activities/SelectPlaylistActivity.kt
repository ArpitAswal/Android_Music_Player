package com.example.playmusic.views.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playmusic.R
import com.example.playmusic.adapter.SongsViewAdapter
import com.example.playmusic.globalclass.AllPlaylistExist
import com.example.playmusic.roomdb.PlaylistRelationship
import com.example.playmusic.views.model.DBViewModel

class SelectPlaylistActivity : AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var subTitle: TextView
    private lateinit var recView: RecyclerView
    private lateinit var myAdapter: SongsViewAdapter
    private lateinit var backBtn: ImageButton
    private lateinit var selectPlaylist: PlaylistRelationship
    private lateinit var dbViewModel: DBViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_playlist)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purpleAccent)
        title = findViewById(R.id.select_playlist_title)
        subTitle = findViewById(R.id.select_playlist_subtitle)
        backBtn = findViewById(R.id.select_playlist_backBtn)
        backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        recView = findViewById(R.id.playlist_songs_recView)
        val layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myAdapter = SongsViewAdapter(
            this@SelectPlaylistActivity,
            AllPlaylistExist.getSelectPlaylist(),
            dbViewModel
        )
        recView.adapter = myAdapter
        recView.layoutManager = layout

        selectPlaylist = AllPlaylistExist.getSelectPlaylist()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()

        title.text = selectPlaylist.playlist.playlistName
        updateSubtitle(selectPlaylist.songs.size)

        dbViewModel.allPlaylist.observe(this, Observer { playlists ->
            playlists?.let {
                AllPlaylistExist.getAllPlaylists(it)
                val updatedPlaylist = AllPlaylistExist.getSelectPlaylist()
                myAdapter.notifyDataSetChanged()
                updateSubtitle(updatedPlaylist.songs.size)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateSubtitle(songCount: Int) {
        subTitle.text = "$songCount songs"
    }
}