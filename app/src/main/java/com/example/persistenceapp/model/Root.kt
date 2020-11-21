package com.example.persistenceapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Root(
        val message: String,
        val cod: String,
        val count: Long,
        val list: List<Element>
)
@Parcelize
data class Element(
        val id: Long,
        val name: String,
        val dt: Long,
        val weather: List<Weather>
) : Parcelable

@Parcelize
data class Weather(
        val id: Long,
        val main: String,
        val description: String,
        val icon: String
) : Parcelable