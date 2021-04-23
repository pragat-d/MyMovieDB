package com.example.mymoviedb

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieGenre (
    @SerializedName("genres")
    var mGenreList : List<GenreDetails> = arrayListOf()
): Parcelable