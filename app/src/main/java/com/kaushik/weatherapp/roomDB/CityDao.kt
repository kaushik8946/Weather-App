package com.kaushik.weatherapp.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY time DESC")
    fun getCities(): List<City>

    @Upsert
    fun upsert(city: City)

    @Delete
    fun delete(city: City)
}