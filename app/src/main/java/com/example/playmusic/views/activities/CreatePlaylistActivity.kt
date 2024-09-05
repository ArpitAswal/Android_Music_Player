package com.example.playmusic.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.playmusic.R
import com.example.playmusic.globalclass.AllPlaylistExist
import com.example.playmusic.dataobject.DBPlaylistData
import com.example.playmusic.views.model.DBViewModel
import kotlinx.coroutines.launch

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var doneBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var dbViewModel: DBViewModel
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)
        val music = AllPlaylistExist.getSongAddTo()

        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        doneBtn = findViewById(R.id.create_playlist_doneBtn)
        cancelBtn = findViewById(R.id.create_playlist_cancelBtn)
        editText = findViewById(R.id.create_playlist_edit)

        doneBtn.setOnClickListener {
            val list = DBPlaylistData(
                playlistName = editText.text.toString(),
                playlistProfile = null,
            )
            lifecycleScope.launch {
                val playlistID: Long = dbViewModel.insertPlaylist(list)
                music.playlistId = playlistID
                dbViewModel.insertSong(music)
                finish()
                // Call finish() in SecondActivity from ThirdActivity
                val intent = Intent(this@CreatePlaylistActivity, MusicPlayerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
            }
        }

        cancelBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_out_top, R.anim.slide_in_bottom)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_out_top, R.anim.slide_in_bottom)
    }
}