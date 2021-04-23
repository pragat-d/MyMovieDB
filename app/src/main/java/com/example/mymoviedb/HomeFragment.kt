package com.example.mymoviedb

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.genre_view.*
import kotlinx.android.synthetic.main.home_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import java.util.zip.Inflater

open class HomeFragment() : Fragment() {
    var popularmovieList: List<GenreMoviesDesc> = arrayListOf()
    var movieResult: GenreMovieResult? = null
    final val api_key: String = "5ee0761a00910666db31f9b99edfd210"
    var mPopularMoviesAdapter: PopularMovieAdapter? = null

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeToolbar.setTitle("Home")
        var popularMovieRecyclerView = popularMoviesRecyclerView
        var linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popularMovieRecyclerView.layoutManager = linearLayoutManager
        mPopularMoviesAdapter = PopularMovieAdapter()
        popularMovieRecyclerView.adapter = mPopularMoviesAdapter
        fetchPopularMovies()
    }

    fun fetchPopularMovies() {
        var retroPopularMovies: Retrofit =
            Retrofit.Builder().baseUrl("https://api.themoviedb.org/").addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        var tmdbJsonHolder: TMDBJsonHolderApi =
            retroPopularMovies.create(TMDBJsonHolderApi::class.java)
        var popularMovieCall: Call<GenreMovieResult> = tmdbJsonHolder.getPopularMovie(api_key)
        popularMovieCall.enqueue(object : Callback<GenreMovieResult> {
            override fun onFailure(call: Call<GenreMovieResult>?, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<GenreMovieResult>?,
                response: Response<GenreMovieResult>?
            ) {
                movieResult = response?.body()
                popularmovieList = movieResult?.results!!
                mPopularMoviesAdapter?.setPopularMovieData(popularmovieList)
            }
        })
    }
}