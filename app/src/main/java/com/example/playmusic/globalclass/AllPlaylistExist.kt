package com.example.playmusic.globalclass

import com.example.playmusic.dataobject.DBMusicData
import com.example.playmusic.roomdb.PlaylistRelationship

object AllPlaylistExist {
    var playlistData = mutableListOf<PlaylistRelationship>()
    var selectedPlaylist = mutableListOf<PlaylistRelationship>()
    private lateinit var songAddTo: DBMusicData
    private lateinit var songPlayTo: DBMusicData

    fun getAllPlaylists(dbPlaylistData: List<PlaylistRelationship>): MutableList<PlaylistRelationship> {
        playlistData.clear()

        for (data in dbPlaylistData) {
            playlistData.add(data)
        }
        return playlistData
    }

    fun removeSelectedPlaylist(list: PlaylistRelationship) {
        selectedPlaylist.remove(list)
    }

    fun setSongAddTo(music: DBMusicData) {
        songAddTo = music
    }

    fun setSongPlayTo(music: DBMusicData) {
        songPlayTo = music
    }

    fun setSelectedPlaylist(list: PlaylistRelationship) {
        selectedPlaylist.add(list)
    }

    fun getSelectsPlaylist(): MutableList<PlaylistRelationship> {
        return selectedPlaylist
    }

    fun getSongPlayTo(): DBMusicData {
        return songPlayTo
    }

    fun getSongAddTo(): DBMusicData {
        return songAddTo
    }

}