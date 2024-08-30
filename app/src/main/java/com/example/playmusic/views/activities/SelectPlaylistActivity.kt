package com.example.playmusic.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playmusic.R

class SelectPlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_playlist)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}