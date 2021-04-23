package com.example.mymoviedb

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymoviedb.PopularMovieAdapter.ViewHolder
import com.example.mymoviedb.R.layout.popular_movie_item
import kotlinx.android.synthetic.main.popular_movie_item.view.*

class PopularMovieAdapter : RecyclerView.Adapter<ViewHolder>() {
    var popularMovieList : MutableList<GenreMoviesDesc> = arrayListOf()
    class ViewHolder(movieView: View) : RecyclerView.ViewHolder(movieView){
        var name = movieView.movieName
        var movieId = movieView.id
        var movieImage = movieView.popularMovieImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var popularMovieItemView = LayoutInflater.from(parent.context).inflate(popular_movie_item,parent,false)
        return ViewHolder(popularMovieItemView)
    }

    override fun getItemCount(): Int {
        return popularMovieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = popularMovieList.get(position)
        holder.movieId = currentItem.id
        holder.name.setText(currentItem.title)
        if(currentItem.poster_path != null){
            Glide.with(holder.itemView.context).load("https://image.tmdb.org/t/p/w500"+currentItem.poster_path)?.into(holder.movieImage)
        }
    }

    fun setPopularMovieData(movieList : List<GenreMoviesDesc>){
        popularMovieList.addAll(movieList)
        notifyDataSetChanged()
    }
}