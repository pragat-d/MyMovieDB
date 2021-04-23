package com.example.mymoviedb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreMovieSearchResult (
    var page : Int = 0,
    var results : List<GenreMoviesDesc>  = arrayListOf()
) : Parcelable