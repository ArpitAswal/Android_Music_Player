package com.example.playmusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playmusic.R
import com.example.playmusic.globalclass.AllPlaylistExist
import com.example.playmusic.roomdb.PlaylistRelationship

class AddPlaylistViewAdapter(
    private val playList: MutableList<PlaylistRelationship>, private val context: Context
) : RecyclerView.Adapter<AddPlaylistViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(context).inflate(
            R.layout.add_playlists_layout, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = playList[position]
        holder.bind(currentData)
        holder.folderName.text = currentData.playlist.playlistName
        if (currentData.playlist.playlistProfile.isNullOrEmpty() && currentData.playlist.playlistName == "Liked Songs") {
            holder.folderImage.setImageResource(R.drawable.favorite_white)
        } else if (!currentData.playlist.playlistProfile.isNullOrEmpty()) {
            Glide.with(context).load(holder.folderImage).error(R.drawable.headphones)
                .into(holder.folderImage)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PlaylistRelationship) {
            folderChecked.isChecked = item.playlist.playlistChecked

            // Handle click or update event
            folderChecked.setOnCheckedChangeListener { _, isChecked ->
                // Update the item's checked state
                if (isChecked) {
                    folderChecked.setBackgroundResource(R.drawable.checked_circle)
                    AllPlaylistExist.setSelectedPlaylist(item)
                } else {
                    folderChecked.setBackgroundResource(R.drawable.unchecked_circle)
                    AllPlaylistExist.removeSelectedPlaylist(item)
                }// Optionally, update the UI if needed
            }
        }

        var folderImage: ImageView
        var folderName: TextView
        private var folderChecked: CheckBox

        init {
            folderImage = itemView.findViewById(R.id.add_playlists_folder_ImgView)
            folderName = itemView.findViewById(R.id.add_playlistTitle)
            folderChecked = itemView.findViewById(R.id.add_playlist_Checkbox)
            AllPlaylistExist.selectedPlaylist.clear()
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    folderChecked.isChecked = !folderChecked.isChecked
                }
            }
        }
    }

}