package com.smdevs.musicplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smdevs.musicplayer.databinding.ActivityMusicBinding
import com.smdevs.musicplayer.model.MusicObject

class MusicActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure the action bar!
        supportActionBar?.title = "Music Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val music = getMusicBundle()
        layoutInit(music)

    }

    private fun layoutInit(music: MusicObject) {
        binding.playerTitle.text = music.title
        binding.playerArtist.text = music.artist

        binding.playerImage.setImageURI(
            Uri.parse(music.albumArt)
        )
    }

    private fun getMusicBundle():MusicObject{
        return intent.getSerializableExtra("music")
            as MusicObject
    }
}