package com.example.persistenceapp.model

@Entity
data class CityDatabase(@PrimaryKey val id: Long, val cityName: String)
