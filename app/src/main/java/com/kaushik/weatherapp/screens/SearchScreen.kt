package com.kaushik.weatherapp.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kaushik.weatherapp.api.WeatherAPI
import com.kaushik.weatherapp.parsing.capitalizeFirstLetterOfEachWord
import com.kaushik.weatherapp.roomDB.City
import com.kaushik.weatherapp.roomDB.CityDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController, db: CityDatabase) {

    val context = LocalContext.current

    var inputCity by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = inputCity,
            onValueChange = {
                inputCity = it
            },
            label = { Text(text = "Enter city name:") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )
        Button(
            onClick = {
                if (inputCity.trim().isEmpty()) {
                    Toast.makeText(context, "enter text!", Toast.LENGTH_SHORT).show()
                } else {
                    validate(context, inputCity, db, navController)
                    inputCity = ""
                }
            }
        ) {
            Text(text = "Search")
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun validate(
    context: Context,
    cityName: String,
    db: CityDatabase,
    navController: NavHostController
) {
    var response: Pair<Boolean, String>? = null

    val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    GlobalScope.launch {
        response = WeatherAPI.getResponse(cityName)
    }

    while (response == null);
    if (response!!.first) {
        val result = response!!.second
        sharedPreferences.edit().putString("result", result).commit()
        GlobalScope.launch {
            db.cityDao().upsert(
                City(capitalizeFirstLetterOfEachWord(cityName.trim()))
            )
        }
        Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show()
        navController.navigate(Result.route)
    } else {
        Toast.makeText(context, response!!.second, Toast.LENGTH_SHORT).show()
    }
}
