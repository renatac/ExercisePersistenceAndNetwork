package com.example.persistenceapp.dao

import androidx.room.*
import com.example.persistenceapp.model.CityDatabase

@Dao
interface CityDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(cityDatabase: CityDatabase)

    @Query("SELECT * FROM citydatabase ORDER BY cityName")
    fun getAllCityDatabase(): List<CityDatabase>

    @Delete
    fun deleteCityDatabaseItem(cityDatabase: CityDatabase)
}