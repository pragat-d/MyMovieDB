package com.example.mymoviedb

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoviedb.GenreGridAdapter.*
import com.example.mymoviedb.R.layout.genre_grid_layout
import kotlinx.android.synthetic.main.genre_grid_layout.view.*
import kotlinx.android.synthetic.main.genre_view.view.*

class GenreGridAdapter(var onGenreClickListener: OnGenreClickListener) : RecyclerView.Adapter<GenreGridViewHolder>() {
    var mGenreList : List<GenreDetails> = arrayListOf()
    class GenreGridViewHolder(var genreGridItem : View) : RecyclerView.ViewHolder(genreGridItem) {
        var genreName : TextView = genreGridItem.genreName
        var genreId : TextView = genreGridItem.genreId
    }

    interface OnGenreClickListener{
        fun onGenreClick(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreGridViewHolder {
        var genreItem = LayoutInflater.from(parent.context).inflate(genre_grid_layout,parent,false)
        return  GenreGridViewHolder(genreItem)
    }

    override fun getItemCount(): Int {
        return mGenreList.size
    }

    override fun onBindViewHolder(holder: GenreGridViewHolder, position: Int) {
        var currentItem = mGenreList.get(position)
        holder.genreName.setText(currentItem.name.toString())
        holder.genreId.setText(currentItem.id.toString())
        holder.genreGridItem.setOnClickListener { onGenreClickListener.onGenreClick(position) }
    }

    fun setGenreData(genreData:List<GenreDetails>){
        mGenreList = genreData
        notifyDataSetChanged()
    }
}