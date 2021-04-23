package com.example.mymoviedb

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreMovieResult (
    var page : Int = 0,
    var results : List<GenreMoviesDesc>  = arrayListOf() ): Parcelable