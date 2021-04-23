package com.example.mymoviedb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpokenLanguage(
    var english_name : String,
    var name : String
) : Parcelable