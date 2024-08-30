package com.example.playmusic.globalclass

import com.example.playmusic.dataobject.PlaylistData

object AllPlaylistExist {
    private var playlistData = mutableListOf<PlaylistData>()
    private var i = 0

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
}