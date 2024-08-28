package com.example.playmusic.dataobject

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class MusicData(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val musicUri: Uri,
    val path: String,
    var lyrics: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readParcelable(Uri::class.java.classLoader) ?: Uri.EMPTY,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeLong(duration)
        parcel.writeParcelable(musicUri, flags)
        parcel.writeString(path)
        parcel.writeString(lyrics)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicData> {
        override fun createFromParcel(parcel: Parcel): MusicData {
            return MusicData(parcel)
        }

        override fun newArray(size: Int): Array<MusicData?> {
            return arrayOfNulls(size)
        }
    }
}
