package com.example.persistenceapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class CityDatabase(
    @PrimaryKey val id: Long,
    val cityName: String,
    val country: String,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable
