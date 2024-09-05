package com.example.playmusic.roomdb

import androidx.room.Embedded
import androidx.room.Relation
import com.example.playmusic.dataobject.DBMusicData
import com.example.playmusic.dataobject.DBPlaylistData

data class PlaylistRelationship(
    @Embedded val playlist: DBPlaylistData,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "playlistId"
    )
    val songs: MutableList<DBMusicData>
)

