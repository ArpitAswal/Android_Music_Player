package com.example.playmusic.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playmusic.R
import com.example.playmusic.views.activities.MusicPlayerActivity
import com.example.playmusic.dataobject.MusicData

class AllSongsViewAdapter(
    private val data: List<MusicData>,
    private var mediaPlayer: MediaPlayer?,
    private var currentlyPlayingPosition: Int,
    private val exoPlayer: ExoPlayer,
    private val context: Context
) : RecyclerView.Adapter<AllSongsViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(context).inflate(
            R.layout.musics_layout, parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = data[position]
        val cover = getAlbum(currentData.path)
        holder.title.text = currentData.title
        Glide.with(context).asBitmap().load(cover).error(R.drawable.headphones).into(holder.image)
        if (position == currentlyPlayingPosition && mediaPlayer?.isPlaying == true) {
            holder.playButton.setImageResource(R.drawable.pause_circle)
        } else {
            holder.playButton.setImageResource(R.drawable.play_icon)
        }

        holder.playButton.setOnClickListener {
            if (currentlyPlayingPosition == position) {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                    holder.playButton.setImageResource(R.drawable.play_icon)

                } else {
                    mediaPlayer?.start()
                    holder.playButton.setImageResource(R.drawable.pause_circle)
                }
            } else {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(context, currentData.musicUri)
                mediaPlayer?.start()
                notifyItemChanged(currentlyPlayingPosition)
                currentlyPlayingPosition = position
                holder.playButton.setImageResource(R.drawable.pause_circle)
            }
        }
    }

    private fun getAlbum(path: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        val title: TextView
        val playButton: ImageButton

        init {
            image = itemView.findViewById(R.id.musicImage)
            title = itemView.findViewById(R.id.musicTitle)
            playButton = itemView.findViewById(R.id.playButton)
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedMusic = data[position]
                    val intent = Intent(itemView.context, MusicPlayerActivity::class.java)
                    intent.putExtra("MusicData", selectedMusic)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}


