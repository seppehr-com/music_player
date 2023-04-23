package com.smdevs.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.smdevs.musicplayer.databinding.ActivityMusicBinding
import com.smdevs.musicplayer.model.MusicObject
import kotlinx.coroutines.*
import kotlinx.coroutines.android.HandlerDispatcher
import java.io.IOException
import java.lang.Exception
import java.lang.Runnable

class MusicActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMusicBinding
    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var runnable: Runnable
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure the action bar!
        supportActionBar?.title = "Music Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val music = getMusicBundle()
        layoutInit(music)
        playerInit(music)
    }

    private fun playerInit(music: MusicObject){
        Log.i("infoLog",Uri.parse(music.data).toString())
            try {
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(applicationContext,Uri.parse(music.data))
                mediaPlayer.prepare()

                binding.playerPlayPause.setOnClickListener {
                    if(mediaPlayer.isPlaying){
                        mediaPlayer.pause()
                        binding.playerPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                    else{
                        mediaPlayer.start()
                        binding.playerPlayPause.setImageResource(R.drawable.ic_baseline_pause_24)
                    }
                }
                seekBarInit()
            }catch (e:Exception){
                Toast.makeText(this,"We couldn't make it!",Toast.LENGTH_LONG).show()
            }
    }

    private fun seekBarInit() {
        binding.playerSeekBar.progress = 0
        binding.playerSeekBar.max = mediaPlayer.duration

        binding.playerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    mediaPlayer.seekTo(progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })


        try {
            runnable = Runnable {
                binding.playerSeekBar.progress=mediaPlayer.currentPosition
                handler.postDelayed(runnable,1000)
            }
            handler.postDelayed(runnable,1000)
        } catch (e:IOException){

        }
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }
}