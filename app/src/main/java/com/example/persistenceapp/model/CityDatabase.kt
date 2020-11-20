package com.example.persistenceapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityDatabase(
    @PrimaryKey val id: Long,
    val cityName: String,
    val country: String,
    val main: String,
    val description: String,
    val icon: String
)
