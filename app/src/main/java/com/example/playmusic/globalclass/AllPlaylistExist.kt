package com.example.playmusic.globalclass

import com.example.playmusic.dataobject.DBMusicData
import com.example.playmusic.roomdb.PlaylistRelationship

object AllPlaylistExist {
    var playlistData = mutableListOf<PlaylistRelationship>()
    var playlistsIds = mutableListOf<PlaylistRelationship>()
    var playlistsRemoveIds = mutableListOf<PlaylistRelationship>()
    var likedSongsList = mutableListOf<DBMusicData>()

    private lateinit var songAddTo: DBMusicData
    private lateinit var songPlayTo: DBMusicData
    private lateinit var selectPlaylist: PlaylistRelationship

    fun addPlaylistId(list: PlaylistRelationship) {
        playlistsIds.add(list)
    }

    fun addPlaylistRemoveIds(list: PlaylistRelationship) {
        playlistsRemoveIds.add(list)
    }

    fun getAllPlaylists(dbPlaylistData: List<PlaylistRelationship>): MutableList<PlaylistRelationship> {
        playlistData.clear()
        likedSongsList.clear()
        for (data in dbPlaylistData) {
            playlistData.add(data)
            if (data.playlist.playlistId == -1L) {
                likedSongsList.addAll(data.songs)
            }
        }
        return playlistData
    }

    fun getAllPlaylistsIds(): MutableList<PlaylistRelationship> {
        return playlistsIds
    }

    fun getPlaylistRemoveIds(): MutableList<PlaylistRelationship> {
        return playlistsRemoveIds
    }

    fun getSongPlayTo(): DBMusicData {
        return songPlayTo
    }

    fun getSongAddTo(): DBMusicData {
        return songAddTo
    }

    fun getSelectPlaylist(): PlaylistRelationship {
        return selectPlaylist
    }

    fun removePlaylistId(list: PlaylistRelationship) {
        playlistsIds.remove(list)
    }

    fun removePlaylistRemoveIds(list: PlaylistRelationship) {
        playlistsRemoveIds.remove(list)
    }

    fun setSongAddTo(music: DBMusicData) {
        songAddTo = music
    }

    fun setSongPlayTo(music: DBMusicData) {
        songPlayTo = music
    }

    fun setSelectPlaylist(list: PlaylistRelationship) {
        selectPlaylist = list
    }
}