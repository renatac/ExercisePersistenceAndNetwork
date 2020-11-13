package com.example.persistenceapp.dao

import com.example.persistenceapp.model.CityDatabase

@Dao
interface CityDatabaseDao {
    @Insert(onConflit = OnConflictStratedy.REPLACE)
    fun save(cityDatabase: CitiDatabase)

    @Query("SELECT * FROM citydatabase ORDER BY cityName DESC")
    fun getAllCityDatabase(): List<CityDatabase>
}