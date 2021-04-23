package com.example.mymoviedb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetail (
    var adult : Boolean,
    var poster_path : String,
    var original_language : String,
    var overview : String,
    var original_title : String,
    var status : String,
    var tagline : String,
    var vote_count : Int,
    var vote_average : Float,
    var runtime : Int
) : Parcelable