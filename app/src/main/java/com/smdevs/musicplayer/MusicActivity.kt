package com.smdevs.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
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

    private var position : Int = 0
    private lateinit var musicArray : ArrayList<MusicObject>
    private lateinit var currentMusic:MusicObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure the action bar!
        supportActionBar?.title = "Music Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setCurrentMusic()

        layoutInit()
        playerInit()
    }

    private fun playerInit(){
            try {
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(applicationContext,Uri.parse(currentMusic.data))
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

                binding.playerNext.setOnClickListener {
                    skipCurrentHandler("next")
                }
                binding.playerPrevious.setOnClickListener {
                    skipCurrentHandler("previous")
                }

                seekBarInit()

                mediaPlayer.setOnCompletionListener {
                    binding.playerPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    binding.playerSeekBar.progress=0
                }
            }catch (e:Exception){
                Toast.makeText(this,"We couldn't make it!",Toast.LENGTH_LONG).show()
            }
    }

    private fun seekBarInit() {
        binding.playerSeekBar.progress = 0
        binding.playerSeekBar.max = mediaPlayer.duration
        binding.playerMaxTime.text= timeConvertor(mediaPlayer.duration)

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
                binding.playerCurrentTime.text = timeConvertor(mediaPlayer.currentPosition)
                handler.postDelayed(runnable,1000)
            }
            handler.postDelayed(runnable,1000)
        } catch (e:IOException){

        }
    }

    private fun layoutInit() {
        binding.playerTitle.text = currentMusic.title
        binding.playerArtist.text = currentMusic.artist

        binding.playerImage.setImageURI(
            Uri.parse(currentMusic.albumArt)
        )
    }

    private fun musicsArrayInit() {
        val bundle = intent.extras
        musicArray = bundle?.getParcelableArrayList("musics")!!
    }

    private fun positionInit(){
        val bundle = intent.extras
        position =  bundle?.getInt("position").toString().toInt()
    }

    private fun skipPositionHandler(action: String){
        if(action == "next" && position < musicArray.size-1)
            position++
        else if(action == "previous" && position>0)
            position--
        else{
            Toast.makeText(applicationContext,"You're might in the first or last audio!",Toast.LENGTH_LONG).show()
        }
    }

    private fun skipCurrentHandler(action : String = "next"){
        skipPositionHandler(action)
        currentMusic = musicArray[position]

        //Update layout
        layoutInit()
        playerInit()
    }


    private fun setCurrentMusic(){
        positionInit()
        musicsArrayInit()

        currentMusic = musicArray[position]
    }

    private fun timeConvertor(second : Int):String{
        var r:Int
        val m:Int = (second / 60).toInt()
        val s:Int = second % 60

        return "${digitMaker(m)}:${digitMaker(s)}"
    }

    private fun digitMaker(digit:Int):String{
        if(digit/10 < 2)
            return "0$digit"
        return "$digit"
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }
}