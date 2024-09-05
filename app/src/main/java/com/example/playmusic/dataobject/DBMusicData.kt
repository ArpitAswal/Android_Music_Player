package com.example.playmusic.dataobject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_data")
data class DBMusicData(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "musicId") val musicId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "album") val album: String,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "musicUri") val musicUri: String,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "lyrics") var lyrics: String,
    @ColumnInfo(name = "playlistId") var playlistId: Long // Foreign key to Playlist
)

