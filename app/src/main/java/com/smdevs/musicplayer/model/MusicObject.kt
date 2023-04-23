package com.smdevs.musicplayer.model

import android.os.Parcel
import android.os.Parcelable

class MusicObject(
    val id:Int,
    val title: String?,
    val artist: String?,
    val duration:Long,
    val data: String?,
    val album: String?,
    val albumId:Int,
    val albumArt: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeLong(duration)
        parcel.writeString(data)
        parcel.writeString(album)
        parcel.writeInt(albumId)
        parcel.writeString(albumArt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicObject> {
        override fun createFromParcel(parcel: Parcel): MusicObject {
            return MusicObject(parcel)
        }

        override fun newArray(size: Int): Array<MusicObject?> {
            return arrayOfNulls(size)
        }
    }
}
