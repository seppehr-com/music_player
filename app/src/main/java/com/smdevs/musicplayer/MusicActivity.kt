package com.smdevs.musicplayer

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

        binding.musicTitle.text = music.title
    }

    private fun getMusicBundle():MusicObject{
        return intent.getSerializableExtra("music")
            as MusicObject
    }
}