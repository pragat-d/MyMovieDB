package com.example.mymoviedb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreMovieSearchDesc(
    var id : Int = 0,
    var title : String = "",
    var poster_path : String = "",
    var vote_average : Double = 0.0,
    var genre_ids : List<Int> = arrayListOf()
) : Parcelable