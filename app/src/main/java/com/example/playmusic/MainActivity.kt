package com.example.playmusic

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.playmusic.globalclass.AllPlaylistExist
import com.example.playmusic.roomdb.PlaylistRelationship
import com.example.playmusic.views.fragments.AllPlaylistsFragment
import com.example.playmusic.views.fragments.AllSongsFragment
import com.example.playmusic.views.model.DBViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var dbViewModel: DBViewModel
    private var playlistSongsList = mutableListOf<PlaylistRelationship>()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Setup TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All Songs"
                1 -> "All Playlists"
                else -> null
            }
        }.attach()

        tabLayout.setTabTextColors(
            ContextCompat.getColor(this, R.color.white),  // Normal state color
            ContextCompat.getColor(this, R.color.purpleAccent)  // Selected state color
        )

        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        dbViewModel.allPlaylist.observe(this) {
            playlistSongsList.clear()
            GlobalScope.launch {
                for (playlist in it) {
                    val id = playlist.playlist.playlistId
                    val songList = dbViewModel.getPlaylistWithSongs(id)
                    playlistSongsList.add(songList)
                }
                AllPlaylistExist.getAllPlaylists(playlistSongsList)
            }
        }
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
}

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllSongsFragment()
            1 -> AllPlaylistsFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}




