package com.smdevs.musicplayer

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.smdevs.musicplayer.adapter.MusicListAdapter
import com.smdevs.musicplayer.databinding.ActivityMainBinding
import com.smdevs.musicplayer.model.MusicObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!checkPermission()){
            return
        }

       recyclerViewInit()

    }

    private fun itemClickHandler(music : MusicObject){
        val bundle = Bundle()
        bundle.putSerializable("music",music)

        val intent = Intent(this,MusicActivity::class.java)
        intent.putExtras(bundle)

        startActivity(intent)
    }

    private fun recyclerViewInit(){
        val musics = musicListInit()

        binding.recyclerViewMusics.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewMusics.adapter = MusicListAdapter(musics,{music:MusicObject->itemClickHandler(music)})
    }

    private fun musicListInit() : List<MusicObject> {
        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"

        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

        val albumArtUri = Uri.parse("content://media/external/audio/albumart");

        val musicList = ArrayList<MusicObject>()

        val audioCursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            proj,
            selection,
            null,
            sortOrder
        )


        try{
            if(audioCursor!=null&& audioCursor.moveToFirst()){
                do{
                    val id = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val title =  audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    val artist = audioCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    val duration = audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    val data = audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                    val album = audioCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                    val albumId=audioCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)

                    musicList.add(
                        MusicObject(
                            id,
                            audioCursor.getString(title),
                            audioCursor.getString(artist),
                            audioCursor.getLong(duration),
                            audioCursor.getString(data),
                            audioCursor.getString(album),
                            albumId,
                            ContentUris.withAppendedId(albumArtUri, audioCursor.getLong(albumId)).toString()
                        )
                    )
                } while (audioCursor.moveToNext())
            }
        } finally{
            audioCursor?.close()
        }
        return musicList
    }

    //Check the permission and request if is not generated!
    private fun checkPermission():Boolean{
        if(ContextCompat.checkSelfPermission(this@MainActivity,Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){
            //Permission has already generated!
            Toast.makeText(this@MainActivity,"Permission has already generated!",Toast.LENGTH_LONG).show()
            return true
        }
        else if(ContextCompat.checkSelfPermission(this@MainActivity,Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) , 12)
            return false
        }
        else{
            Toast.makeText(this@MainActivity,"You should enable this permission from settings!",Toast.LENGTH_LONG).show()
            return false
        }
    }
}