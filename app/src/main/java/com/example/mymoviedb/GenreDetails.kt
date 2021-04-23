package com.example.mymoviedb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreDetails (
    var id : Int = 0,
    var name : String = ""
) : Parcelable
