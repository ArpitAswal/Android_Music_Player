package com.example.playmusic.globalclass

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.playmusic.dataobject.DBMusicData

object DeviceMusic {
    val musicList = mutableListOf<DBMusicData>()

    fun loadMusicFiles(context: Context) {
        musicList.clear()
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            do {
                val musicId = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val duration = cursor.getLong(durationColumn)
                val path = cursor.getString(pathColumn)

                val musicUri = android.content.ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    musicId
                )

                musicList.add(
                    DBMusicData(
                        0,
                        musicId,
                        title,
                        artist,
                        album,
                        duration,
                        musicUri.toString(),
                        path,
                        "",
                        0L
                    )
                )
            } while (cursor.moveToNext())
            cursor.close()
        }
    }

}
