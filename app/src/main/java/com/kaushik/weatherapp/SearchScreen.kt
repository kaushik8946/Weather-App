package com.kaushik.weatherapp

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController) {

    // Get the current context
    val context = LocalContext.current

    // Create a rememberSaveable state variable to store the city name entered by the user
    val inputCity = rememberSaveable { mutableStateOf("") }

    // Show a column with the following children:
    // - An outlined text field where the user can enter the city name
    // - A button that the user can click to search for the weather in the entered city
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = inputCity.value, // The value of the text field
            onValueChange = { inputCity.value = it }, // The callback that is called when the value of the text field changes
            label = { Text(text = "Enter city name:") }, // The label of the text field
            singleLine = true, // Whether the text field should allow multiple lines
            shape = RoundedCornerShape(8.dp) // The shape of the text field
        )
        Button(onClick = {
            // If the city name is empty, show a toast message and do nothing else
            if (inputCity.value.isEmpty()) {
                Toast.makeText(context, "enter text!", Toast.LENGTH_SHORT).show()
            } else {
                // Validate the city name
                validate(context, navController, inputCity.value)

                // Clear the city name text field
                inputCity.value = ""
            }
        }) {
            Text(text = "Search") // The text of the button
        }
    }
}

// Opt-in to the DelicateCoroutinesApi class, as we will be using GlobalScope.launch.
@OptIn(DelicateCoroutinesApi::class)
fun validate(
    context: Context,
    navController: NavHostController,
    city: String
) {
    // Declare a variable to store the response from the WeatherAPI.
    var response: Pair<Boolean, String>? = null

    // Get the SharedPreferences object.
    val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)

    // Launch a coroutine to get the response from the WeatherAPI.
    GlobalScope.launch {
        response = WeatherAPI.getResponse(city)
    }

    // Wait for the response to be available.
    while (response == null) {
    }

    // Check if the response was successful.
    if (response!!.first) {
        // Get the result from the response.
        val result = response!!.second

        // Store the result in SharedPreferences.
        sharedPreferences.edit().putString("result", result).commit()

        // Convert the result to a map.
        val allData = jsonToMap(result)

        // Get the city from the map.
        val city = allData?.get("name").toString()

        // Get the list of cities from SharedPreferences.
        val citySet = sharedPreferences.getStringSet("cityList", mutableSetOf())?.toMutableList()

        // Convert the list of cities to a set.
        val set = citySet?.toMutableSet()

        // Add the new city to the set.
        set?.add(city)

        // Store the updated list of cities in SharedPreferences.
        sharedPreferences.edit().putStringSet("cityList", set).commit()

        // Show a toast message indicating that the validation was successful.
        Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show()

        // Navigate to the Result screen.
        navController.navigate(Result.route)
    } else {
        // Show a toast message indicating that the validation failed.
        Toast.makeText(context, response!!.second, Toast.LENGTH_SHORT).show()
    }
}

