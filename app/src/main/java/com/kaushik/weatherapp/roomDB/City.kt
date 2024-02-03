package com.kaushik.weatherapp.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "time") val time: Long = System.currentTimeMillis()
)