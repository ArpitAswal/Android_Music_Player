package com.example.playmusic.globalclass

import com.example.playmusic.dataobject.MusicData
import com.example.playmusic.dataobject.PlaylistData

object AllPlaylistExist {
    private var playlistData = mutableListOf<PlaylistData>()
    private var favListData = mutableListOf<MusicData>()
    var indexOfLists = mutableListOf<Int>()
    lateinit var songAddTo: MusicData
    fun getAllPlaylists() : MutableList<PlaylistData>{
        playlistData.clear()
            playlistData.add(
                PlaylistData(
                    id = -1,
                    title = "Liked Songs",
                    image = "",
                    isChecked = false
                )
            )
        return playlistData
    }

    fun getAllLiked() : MutableList<MusicData>{
         return favListData
    }

    fun addLikedSong(music: MusicData){
        favListData.add(music)
    }

    fun removeLikedSong(music: MusicData){
        favListData.remove(music)
    }

    fun addSongsInLists() {
        favListData.add(songAddTo)
    }
}