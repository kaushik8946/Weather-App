package com.kaushik.weatherapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kaushik.weatherapp.roomDB.City
import com.kaushik.weatherapp.roomDB.CityDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

var citiesList = mutableStateOf(listOf<City>())

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class, DelicateCoroutinesApi::class)
@Composable
fun HistoryScreen(navController: NavHostController, db: CityDatabase) {
    val context = LocalContext.current
    GlobalScope.launch {
        citiesList = mutableStateOf(db.cityDao().getCities())
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        item {
            if (citiesList.value.isEmpty()) {
                Text(
                    text = "No history found",
                    fontSize = 20.sp
                )
            } else {
                Text(
                    text = "Click on a city name to get weather info",
                    fontSize = 15.sp
                )
            }

            citiesList.value.forEach { city ->
                Card(
                    onClick = {
                        validate(context, city.name, db, navController)
                    },
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .height(40.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = 10.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = city.name,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .align(Alignment.CenterStart),
                            fontSize = 20.sp
                        )
                        IconButton(
                            onClick = {
                                GlobalScope.launch {
                                    db.cityDao().delete(city)
                                    citiesList.value = db.cityDao().getCities()
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
                        }
                    }
                }
            }
        }
    }
}
