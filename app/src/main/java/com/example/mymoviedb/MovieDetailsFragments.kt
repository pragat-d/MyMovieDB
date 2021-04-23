package com.example.mymoviedb

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.mymoviedb.R.drawable.ic_arrow_back
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.details_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailsFragments : Fragment(){

    var mMovieId : Int = 0
    var mBaseUrl : String = "https://api.themoviedb.org/"
    final val mAPIKey : String = "5ee0761a00910666db31f9b99edfd210"
    var mMovieDetail : MovieDetail?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_movie,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailToolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)
        movieDetailToolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var fragmentManager : FragmentManager = activity?.supportFragmentManager!!
                fragmentManager.popBackStackImmediate()
            }
        })
        fetchData()
    }

    private fun fetchData() {
        mMovieId = arguments?.getInt("movieId")!!
        var genreMoviesRetrofit : Retrofit = Retrofit.Builder().baseUrl(mBaseUrl).addConverterFactory(
            GsonConverterFactory.create()).build()
        var tmdbJsonHolderApi : TMDBJsonHolderApi = genreMoviesRetrofit.create(TMDBJsonHolderApi::class.java)
        var genreMoviesCall : Call<MovieDetail> = tmdbJsonHolderApi.getMoviesById(mMovieId,mAPIKey)
        genreMoviesCall.enqueue(object : Callback<MovieDetail> {
            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                Toast.makeText(context,"Error : "+t?.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<MovieDetail>?,
                response: Response<MovieDetail>?
            ) {
                if(!response?.isSuccessful!!){
                    Toast.makeText(context,response.message(), Toast.LENGTH_LONG)
                    return;
                }
                if (response != null){
                    mMovieDetail = response?.body()!!
                    Glide.with(activity?.applicationContext!!)?.load("https://image.tmdb.org/t/p/w500"+ mMovieDetail?.poster_path.toString())?.into(movieImages)
                    movieRatingBar.rating = mMovieDetail?.vote_average!! /2
                    if(mMovieDetail?.adult == true)
                        adultRatingResponse.setText("YES")
                    else{
                        adultRatingResponse.setText("NO")
                    }

                    overViewResponse.setText(mMovieDetail?.overview)

                    var collapsingToolbarLayout :CollapsingToolbarLayout = collapsingToolbar

                    collapsingToolbarLayout.isTitleEnabled = true
                    collapsingToolbarLayout.title = mMovieDetail?.original_title.toString()

                    statusResponse.setText(mMovieDetail?.status.toString())

                    rumtimeResponse.setText(mMovieDetail?.runtime.toString())
                }
            }
        })
    }
}