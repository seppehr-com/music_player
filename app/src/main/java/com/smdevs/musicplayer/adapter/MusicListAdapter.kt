package com.smdevs.musicplayer.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.smdevs.musicplayer.R
import androidx.recyclerview.widget.RecyclerView
import com.smdevs.musicplayer.model.MusicObject

class MusicListAdapter(val musics : List<MusicObject>,val itemClickHandler:(music: MusicObject)->Unit) : RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {
    inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val itemImage : ImageView = view.findViewById(R.id.itemImage)
        val itemTitle : TextView = view.findViewById(R.id.itemTitle)
        val itemArtist : TextView = view.findViewById(R.id.itemArtist)
        val itemWrapper : LinearLayout = view.findViewById(R.id.itemWrapper)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = musics[position].title
        holder.itemArtist.text = musics[position].artist

        holder.itemImage.setImageURI(
            Uri.parse(musics[position].albumArt)
        )

        holder.itemWrapper.setOnClickListener {
            itemClickHandler(musics[position])
        }
    }

    override fun getItemCount(): Int = musics.size
}
