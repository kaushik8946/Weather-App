package com.kaushik.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.kaushik.weatherapp.roomDB.CityDatabase
import com.kaushik.weatherapp.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db by lazy {
                Room.databaseBuilder(
                    applicationContext,
                    CityDatabase::class.java,
                    "cities.db"
                ).build()
            }
            // Calling the MainScreen function since it contains all the UI
            MainScreen(db)
        }
    }
}
