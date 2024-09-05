package com.example.playmusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playmusic.R
import com.example.playmusic.roomdb.PlaylistRelationship
import com.example.playmusic.views.model.DBViewModel
import kotlinx.coroutines.launch

class PlaylistViewAdapter(
    private val playList: MutableList<PlaylistRelationship>,
    private val context: Context,
    private val dbViewModel: DBViewModel
) : RecyclerView.Adapter<PlaylistViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(context).inflate(
            R.layout.playlists_layout, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = playList[position]
        holder.folderName.text = currentData.playlist.playlistName
        if (currentData.playlist.playlistProfile.isNullOrEmpty() && currentData.playlist.playlistName == "Liked Songs") {
            holder.folderImage.setImageResource(R.drawable.favorite_white)
            holder.folderDelete.visibility = View.GONE
        } else {
            Glide.with(context).load(holder.folderImage).error(R.drawable.headphones)
                .into(holder.folderImage)
            holder.folderDelete.visibility = View.VISIBLE
        }
        holder.folderSongs.text = "Playlist ${currentData.songs.size} songs"
        holder.folderDelete.setOnClickListener {
            dbViewModel.viewModelScope.launch {
                dbViewModel.deletePlaylist(currentData.playlist)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folderImage: ImageView
        var folderName: TextView
        var folderSongs: TextView
        var folderDelete: ImageButton

        init {
            folderImage = itemView.findViewById(R.id.playlists_folder_ImgView)
            folderName = itemView.findViewById(R.id.playlistTitle)
            folderSongs = itemView.findViewById(R.id.playlistSubTitle)
            folderDelete = itemView.findViewById(R.id.playlist_remove_Btn)
            itemView.setOnClickListener {
                val position = adapterPosition
            }
        }
    }
}