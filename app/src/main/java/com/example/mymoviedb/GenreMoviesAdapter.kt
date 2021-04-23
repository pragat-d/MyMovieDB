package com.example.mymoviedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*

class GenreMoviesAdapter(var mMovieOnClickListener: OnMovieClickListener) : Adapter<GenreMoviesAdapter.GenreMoviesViewHolder>() {
    var mImageBaseUrl = ""
    var mGenreMovieList : List<GenreMoviesDesc> = arrayListOf()
    var mImageUrl : String = ""
    class GenreMoviesViewHolder(var genreMoviesItemView : View) : RecyclerView.ViewHolder(genreMoviesItemView) {
        var mMovieName = genreMoviesItemView.genreMovieName
        var mMovieImage = genreMoviesItemView.genreMovieImage
        var mMovieRating = genreMoviesItemView.ratingBar
    }

    interface OnMovieClickListener {
        fun onMovieClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreMoviesViewHolder {
        var movieItemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)
        return GenreMoviesViewHolder(movieItemView)
    }

    override fun getItemCount(): Int {
       return mGenreMovieList.size
    }

    override fun onBindViewHolder(holder: GenreMoviesViewHolder, position: Int) {
        holder.mMovieName.setText(mGenreMovieList.get(position).title)
        val movie = mGenreMovieList.get(position)
        if(movie?.poster_path != null){
            Glide.with(holder.itemView.context)?.load("https://image.tmdb.org/t/p/w500"+ movie?.poster_path.toString())?.into(holder.mMovieImage)
        }
        holder.mMovieRating.rating = mGenreMovieList.get(position).vote_average.toFloat()/2
        holder.itemView.setOnClickListener {mMovieOnClickListener.onMovieClickListener(position)}
    }

    fun setGenreMovies(genreMovies : List<GenreMoviesDesc>){
        mGenreMovieList = genreMovies
        notifyDataSetChanged()
    }

    fun clearData(){
        mGenreMovieList = arrayListOf()
        notifyDataSetChanged()
    }

}