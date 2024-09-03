package com.example.playmusic.views.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.playmusic.dataobject.DBMusicData
import com.example.playmusic.dataobject.DBPlaylistData
import com.example.playmusic.globalclass.AllPlaylistExist
import com.example.playmusic.roomdb.AppDatabase
import com.example.playmusic.roomdb.PlaylistDAO
import com.example.playmusic.roomdb.PlaylistRelationship
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DBViewModel(application: Application) : AndroidViewModel(application) {
    var allPlaylist: LiveData<List<PlaylistRelationship>>
    private var dao: PlaylistDAO

    init {
        dao = AppDatabase.getDatabase(application).playlistDao()
        allPlaylist = dao.getPlaylistsWithSongs()
    }

    suspend fun deletePlaylist(list: DBPlaylistData) = viewModelScope.launch {
        dao.deleteSongsInPlaylist(list.playlistId).run {
            dao.deletePlaylist(list)
        }
    }

    suspend fun getPlaylistWithSongs(id: Long) = withContext(Dispatchers.IO) {
        dao.getPlaylistWithSong(id)
    }

    suspend fun insertPlaylist(list: DBPlaylistData) = withContext(Dispatchers.IO) {
        dao.insertPlaylist(list)
    }

    suspend fun insertSong(song: DBMusicData) = viewModelScope.launch {
        dao.insertSong(song)
    }

    suspend fun insertSongInMultipleList() = viewModelScope.launch {
        val songAddTo: DBMusicData = AllPlaylistExist.getSongAddTo()
        for (list in AllPlaylistExist.getSelectsPlaylist()) {
            val id = list.playlist.playlistId
            songAddTo.playlistId = id
            dao.insertSong(songAddTo)
        }
    }

}