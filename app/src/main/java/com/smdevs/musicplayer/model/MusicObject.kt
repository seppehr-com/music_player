package com.smdevs.musicplayer.model

import java.io.Serializable

data class MusicObject(
    val  id:Int,
    val title:String,
    val artist:String,
    val duration:Long,
    val data:String,
    val album:String,
    val albumId:Int,
    val albumArt:String,
) : Serializable
