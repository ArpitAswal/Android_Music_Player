package com.example.playmusic.dataobject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlayListTable")
data class DBPlaylistData(
    @PrimaryKey(autoGenerate = true) val playlistId: Long = 0,
    @ColumnInfo(name = "playlistName") val playlistName: String?,
    @ColumnInfo(name = "playlistProfile") val playlistProfile: String?,
    @ColumnInfo(name = "playlistChecked") val playlistChecked: Boolean = false
)

