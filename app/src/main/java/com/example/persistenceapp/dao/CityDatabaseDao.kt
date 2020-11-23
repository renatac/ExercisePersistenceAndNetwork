package com.example.persistenceapp.dao

import androidx.room.*
import com.example.persistenceapp.model.CityDatabase
import com.example.persistenceapp.model.CitySearchDatabase

@Dao
interface CityDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(cityDatabase: CityDatabase)

    @Query("SELECT * FROM citydatabase ORDER BY cityName")
    fun getAllCityDatabase(): List<CityDatabase>

    @Delete
    fun deleteCityDatabaseItem(cityDatabase: CityDatabase)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveSearch(citySearchDatabase: List<CitySearchDatabase>)

    @Query("SELECT * FROM citysearchdatabase")
    fun getAllSearchDatabase(): List<CitySearchDatabase>

    @Delete
    fun deleteAllSearchDatabase(citySearchDatabase: CitySearchDatabase)
}