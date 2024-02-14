package com.kaushik.weatherapp.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kaushik.weatherapp.R
import com.kaushik.weatherapp.parsing.getCurrentTimeInTimeZone
import com.kaushik.weatherapp.parsing.getDescription
import com.kaushik.weatherapp.parsing.getIcon
import com.kaushik.weatherapp.parsing.getSpecifiedTimeInTimeZone
import com.kaushik.weatherapp.parsing.jsonToMap

@Composable
fun ResultScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    val result = sharedPreferences.getString("result", null)

    if (result != null) {
        val allData = jsonToMap(result.toString())
        val city = allData?.get("name").toString().uppercase()
        val mainData = jsonToMap(allData?.get("main").toString())
        var weather = allData?.get("weather").toString()
        weather = weather.substring(1, weather.length - 1)
        val icon = try {
            val weatherData = jsonToMap(weather)
            weatherData?.get("icon").toString()
        } catch (e: Exception) {
            getIcon(weather).toString()
        }

        val description = try {
            val weatherData = jsonToMap(weather)
            weatherData?.get("description").toString()
        } catch (e: Exception) {
            getDescription(weather).toString()
        }

        val timeZone = allData?.get("timezone").toString().toDouble().toInt()

        val time = getCurrentTimeInTimeZone(timeZone)

        val sysData = jsonToMap(allData?.get("sys").toString())
        val sunrise = sysData?.get("sunrise").toString().toDouble().toLong()
        val sunset = sysData?.get("sunset").toString().toDouble().toLong()

        val sunriseTime = getSpecifiedTimeInTimeZone(timeZone, sunrise)
        val sunsetTime = getSpecifiedTimeInTimeZone(timeZone, sunset)

        val wind = jsonToMap(allData?.get("wind").toString())
        val windSpeed = wind?.get("speed").toString().toDouble() * (18 / 5) // Get Wind speed in KM/h

        // Show the weather data
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = city, fontSize = 20.sp)
            Text(text = time, fontSize = 20.sp)
            val iconurl = "https://openweathermap.org/img/wn/$icon@4x.png"
            AsyncImage(
                model = iconurl,
                contentDescription = "icon",
                modifier = Modifier.size(300.dp)
            )
            val temp = mainData?.get("temp").toString().toDouble() - 273.15
            Text(text = String.format("%.2f", temp) + "°C", fontSize = 40.sp)
            Text(text = description.uppercase(), fontSize = 24.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunrise),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = "SUNRISE")
                    Text(text = sunriseTime)
                }
                Column(
                    modifier = Modifier.fillMaxWidth(.3f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunset),
                        contentDescription = "sunset",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = "SUNSET")
                    Text(text = sunsetTime)
                }
                Column(
                    modifier = Modifier.fillMaxWidth(.3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.wind),
                        contentDescription = "wind",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = "WIND")
                    Text(
                        text = String.format("%.2f", windSpeed)+"\nKM/H",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "No search yet",
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
