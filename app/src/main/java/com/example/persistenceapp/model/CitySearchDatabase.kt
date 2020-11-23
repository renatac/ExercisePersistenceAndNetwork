package com.example.persistenceapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class CitySearchDatabase(
    @PrimaryKey
    val id: Long,
    val name: String,
    val dt: Long,
    val weatherId: Long,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String

) : Parcelable
