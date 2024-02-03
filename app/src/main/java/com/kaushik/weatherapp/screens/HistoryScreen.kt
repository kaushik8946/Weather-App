package com.kaushik.weatherapp.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(navController: NavHostController) {
    // Get the shared preferences to access the list of cities
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    val citiesSet: MutableSet<String>? = sharedPreferences.getStringSet("cityList", setOf())
    val citiesList: List<String> = citiesSet?.toList() ?: emptyList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        // If there are no cities in the history, show a message
        item {
            if (citiesList.isEmpty()) {
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

            // Iterate over the list of cities and show a card for each city
            citiesList.forEach { city ->
                Card(
                    onClick = {
                        validate(context, navController, city)
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
                            text = city,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .align(Alignment.CenterStart),
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}
