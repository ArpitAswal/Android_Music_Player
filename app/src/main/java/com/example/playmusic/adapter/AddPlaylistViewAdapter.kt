package com.example.playmusic.adapter

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
import com.example.playmusic.dataobject.PlaylistData

class AddPlaylistViewAdapter(
    private val playList: MutableList<PlaylistData>,
    private val context: Context
) : RecyclerView.Adapter<AddPlaylistViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(context).inflate(
            R.layout.playlists_layout, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = playList[position]
        holder.bind(currentData)
        holder.folderName.text = currentData.title
        if(currentData.image.isEmpty() && currentData.title == "Liked Songs"){
            holder.folderImage.setImageResource(R.drawable.favorite)
        } else if(currentData.image.isNotEmpty()) {
            Glide.with(context).load(holder.folderImage).error(R.drawable.headphones)
                .into(holder.folderImage)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folderImage: ImageView
        var folderName: TextView
        var folderChecked: CheckBox

        init {
            folderImage = itemView.findViewById(R.id.playlists_folder_ImgView)
            folderName = itemView.findViewById(R.id.playlistTitle)
            folderChecked = itemView.findViewById(R.id.playlist_Checkbox)
            itemView.setOnClickListener {

            }

        }

        fun bind(item: PlaylistData) {
            // Set the initial state of the checkbox
            folderChecked.isChecked = item.isChecked

            // Handle click or update event
            folderChecked.setOnCheckedChangeListener { _, isChecked ->
                // Update the item's checked state
                if(isChecked)
                    folderChecked.setBackgroundResource(R.drawable.checked_circle)
                else
                    folderChecked.setBackgroundResource(R.drawable.unchecked_circle)
                item.isChecked = isChecked
                // Optionally, update the UI if needed
                folderChecked.isChecked = isChecked
            }
        }
    }

}