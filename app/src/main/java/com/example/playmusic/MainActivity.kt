package com.example.playmusic

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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

    private var backPressedOnce = false
    private lateinit var toolbar: Toolbar
    private lateinit var dbViewModel: DBViewModel
    private var playlistSongsList = mutableListOf<PlaylistRelationship>()
    private val backPressHandler = Handler(Looper.getMainLooper())
    private val backPressRunnable = Runnable { backPressedOnce = false }
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var searchView: SearchView
    private lateinit var fragment: Fragment

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // Get the fragment at the selected position
                fragment = getFragmentAtPosition(position)!!
            }
        })

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

    // Retrieve the fragment instance from the adapter
    private fun getFragmentAtPosition(position: Int): Fragment? {
        return adapter.getFragmentAt(position)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed();
        } else if (backPressedOnce) {
            super.onBackPressed()
            return
        }

        this.backPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        // Reset the backPressedOnce flag after 3 seconds
        backPressHandler.postDelayed(backPressRunnable, 3000)
    }

    // method to inflate the options menu when the user opens the menu for the first time
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        // below line is to get our menu item.
        val searchItem: MenuItem = menu.findItem(R.id.search)

        // getting search view of our item.
        searchView = searchItem.actionView as SearchView

        // Change search text color
        val searchTextView =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchTextView.setTextColor(ContextCompat.getColor(this, R.color.white))  // Set text color
        searchTextView.setHintTextColor(
            ContextCompat.getColor(
                this,
                R.color.grey
            )
        )  // Set hint color
        searchTextView.hint = "Search..."

        searchTextView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        // Change close icon color
        val closeIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeIcon.setColorFilter(ContextCompat.getColor(this, R.color.white))

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (fragment is AllSongsFragment) {
                    // calling a method to filter our recycler view.
                    (fragment as AllSongsFragment).filter(query.toString(), true)
                } else (fragment as? AllPlaylistsFragment)?.playlistFilter(query.toString(), true)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // Check if the fragment is an instance of AllSongsFragment
                if (fragment is AllSongsFragment) {
                    // calling a method to filter our recycler view.
                    (fragment as AllSongsFragment).filter(newText, false)
                } else (fragment as? AllPlaylistsFragment)?.playlistFilter(newText, false)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu);
    }
}

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val registeredFragments = SparseArray<Fragment>()
    override fun getItemCount(): Int {
        return 2 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = AllSongsFragment()
            1 -> fragment = AllPlaylistsFragment()
        }

        // Store fragment instance
        registeredFragments.put(position, fragment)
        return fragment!!
    }

    // Method to retrieve fragment by position
    fun getFragmentAt(position: Int): Fragment? {
        return registeredFragments[position]
    }
}






