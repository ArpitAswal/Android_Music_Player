package com.example.playmusic.adapter

import android.media.MediaMetadataRetriever
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
import com.example.playmusic.views.activities.SelectPlaylistActivity
import com.example.playmusic.views.model.DBViewModel
import kotlinx.coroutines.launch

class SongsViewAdapter(
    val context: SelectPlaylistActivity,
    val playlist: PlaylistRelationship,
    private val dbViewModel: DBViewModel
) : RecyclerView.Adapter<SongsViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.musics_layout, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return playlist.songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSong = playlist.songs[position]
        val cover = getAlbum(currentSong.path)
        Glide.with(context).asBitmap().load(cover).error(R.drawable.headphones)
            .into(holder.songImage)
        holder.songName.text = currentSong.title
        holder.songArtist.text = currentSong.artist
        holder.songDelete.setImageResource(R.drawable.music_delete)
        holder.songDelete.visibility = View.VISIBLE
        holder.songDelete.setOnClickListener {
            dbViewModel.viewModelScope.launch {
                dbViewModel.deleteSongFromPlaylist(currentSong.playlistId, currentSong.musicId)
                removeSongAt(position)
            }
        }
    }

    // Remove the song from the list and notify the adapter
    private fun removeSongAt(position: Int) {
        playlist.songs.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, playlist.songs.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var songImage: ImageView
        var songName: TextView
        var songArtist: TextView
        val songDelete: ImageButton

        init {
            songImage = itemView.findViewById(R.id.musicImage)
            songName = itemView.findViewById(R.id.musicTitle)
            songArtist = itemView.findViewById(R.id.musicSubTitle)
            songDelete = itemView.findViewById(R.id.playButton)
        }
    }

    private fun getAlbum(path: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }
}