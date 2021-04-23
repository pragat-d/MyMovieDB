package com.example.mymoviedb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBJsonHolderApi {

    @GET("3/genre/movie/list")
    fun getGenre(
        @Query("api_key")  apiKey : String
    ) : Call<MovieGenre>

    @GET("3/discover/movie")
    fun getMoviesPerGenre(
        @Query("api_key")  apiKey : String,
        @Query("with_genres") genre : Int,
        @Query("page") pageIndex : Int
    ) : Call<GenreMovieResult>


    @GET("3/movie/{movie_id}")
    fun getMoviesById(
        @Path("movie_id") movieId : Int,
        @Query("api_key") apiKey : String
    ) : Call<MovieDetail>

    @GET("3/movie/popular")
    fun getPopularMovie(
        @Query("api_key") apiKey : String
    ) : Call<GenreMovieResult>

    @GET("3/search/movie")
    fun getSearchMovies(
        @Query("api_key")  apiKey : String,
        @Query("query") searchString : String,
        @Query("page") pageIndex : Int
    ) : Call<GenreMovieSearchResult>


}