package com.example.persistenceapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.persistenceapp.model.CityDatabase

@Dao
interface CityDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(cityDatabase: CityDatabase)

    @Query("SELECT * FROM citydatabase ORDER BY cityName")
    fun getAllCityDatabase(): List<CityDatabase>
}