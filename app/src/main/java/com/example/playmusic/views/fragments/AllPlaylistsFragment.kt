package com.example.playmusic.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playmusic.R
import com.example.playmusic.adapter.PlaylistViewAdapter
import com.example.playmusic.globalclass.AllPlaylistExist
import com.example.playmusic.views.model.DBViewModel

class AllPlaylistsFragment : Fragment() {

    private lateinit var rec: RecyclerView
    private lateinit var myAdapter: PlaylistViewAdapter
    private lateinit var dbViewModel: DBViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rec = view.findViewById(R.id.all_playlist_recView)
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        val layoutManager =
            LinearLayoutManager(requireView().context, LinearLayoutManager.VERTICAL, false)
        myAdapter = PlaylistViewAdapter(
            AllPlaylistExist.playlistData,
            requireView().context,
            dbViewModel
        )
        rec.adapter = myAdapter
        rec.layoutManager = layoutManager
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()

        dbViewModel.allPlaylist.observe(viewLifecycleOwner, Observer { playlists ->
            playlists?.let {
                AllPlaylistExist.getAllPlaylists(it)
                myAdapter.notifyDataSetChanged()
            }
        })
    }
}
