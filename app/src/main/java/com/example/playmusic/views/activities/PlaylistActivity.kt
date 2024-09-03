package com.example.playmusic.views.activities

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playmusic.R
import com.example.playmusic.adapter.AddPlaylistViewAdapter
import com.example.playmusic.globalclass.AllPlaylistExist
import com.example.playmusic.dataobject.DBMusicData
import com.example.playmusic.roomdb.PlaylistRelationship
import com.example.playmusic.views.model.DBViewModel
import kotlinx.coroutines.launch

class PlaylistActivity : AppCompatActivity() {
    private lateinit var backBtn: ImageButton
    private lateinit var newPlaylist: Button
    private lateinit var doneBtn: Button
    private lateinit var myRecView: RecyclerView
    private lateinit var myAdapter: AddPlaylistViewAdapter
    private lateinit var playlistList: MutableList<PlaylistRelationship>
    private lateinit var dbViewModel: DBViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        myRecView = findViewById(R.id.create_playlist_recView)
        backBtn = findViewById(R.id.playlist_backBtn)
        newPlaylist = findViewById(R.id.new_playlist_btn)
        doneBtn = findViewById(R.id.done_BtnView)
        val drawable = newPlaylist.background as GradientDrawable
        drawable.setColor(resources.getColor(R.color.white, null))
        // Set the border (stroke) color dynamically
        drawable.setStroke(2, resources.getColor(R.color.black))
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]

        backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
        }

        newPlaylist.setOnClickListener {
            val intent = Intent(this@PlaylistActivity, CreatePlaylistActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
        }

        doneBtn.setOnClickListener {
            if (AllPlaylistExist.getSelectsPlaylist().isNotEmpty()) {
                lifecycleScope.launch {
                    dbViewModel.insertSongInMultipleList()
                }
                finish()
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom)
            }
        }

        playlistList = AllPlaylistExist.playlistData
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