package com.example.playmusic.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playmusic.dataobject.DBMusicData
import com.example.playmusic.dataobject.DBPlaylistData

@Dao
interface PlaylistDAO {

    // Insert a new playlist and return its ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: DBPlaylistData): Long

    // Insert a single song into a playlist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: DBMusicData)

    //This method will return the count of songs that match both the musicId and playlistId. If the count is greater than 0, the song is already in the playlist.
    @Query("SELECT COUNT(*) FROM music_data WHERE musicId = :musicId AND playlistId = :playlistId")
    suspend fun isSongInPlaylist(musicId: Long, playlistId: Long): Int

    // Get all playlists with their songs, sorted by title
    @Transaction
    @Query("SELECT * FROM PlayListTable ORDER BY playlistName ASC")
    fun getPlaylistsWithSongs(): LiveData<List<PlaylistRelationship>>

    // Get a specific playlist with its songs
    @Transaction
    @Query("SELECT * FROM PlayListTable WHERE playlistId = :playlistId")
    fun getPlaylistWithSong(playlistId: Long): PlaylistRelationship

    @Delete
    suspend fun deletePlaylist(playlist: DBPlaylistData)

    // Delete all songs in a specific playlist
    @Query("DELETE FROM music_data WHERE playlistId = :playlistId")
    suspend fun deleteSongsInPlaylist(playlistId: Long)

    // Delete a specific song from a specific playlist
    @Query("DELETE FROM music_data WHERE playlistId = :playlistId AND musicId = :musicId")
    suspend fun deleteSongFromPlaylist(playlistId: Long, musicId: Long)
}
