package com.kaushik.weatherapp

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.gson.Gson
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

@Composable
fun ResultScreen() {
    // Get the shared preferences to access the result
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    val result = sharedPreferences.getString("result", null)

    // If there is a result, show it
    if (result != null) {
        // Parse the JSON result into a map
        val allData = jsonToMap(result.toString())

        // Get the city name
        val city = allData?.get("name").toString().uppercase()

        // Get the main weather data
        val mainData = jsonToMap(allData?.get("main").toString())

        // Get the weather data
        var weather = allData?.get("weather").toString()
        weather = weather.substring(1, weather.length - 1)

        // Try to get the weather icon from the weather data
        val icon = try {
            val weatherData = jsonToMap(weather)
            weatherData?.get("icon").toString() // Try to get the icon from the weather data
        } catch (e: Exception) {
            getIcon(weather).toString() // If the icon is not available, get it from the weather description
        }

        // Get the weather description
        val description = try {
            val weatherData = jsonToMap(weather)
            weatherData?.get("description").toString() // Try to get the description from the weather data
        } catch (e: Exception) {
            getDescription(weather).toString() // If the description is not available, get it from the weather
        }

        // Get the time zone offset
        val timeZone = allData?.get("timezone").toString().toDouble().toInt()

        // Get the current time in the time zone
        val time = getCurrentTimeInTimeZone(timeZone)

        // Get the sunrise and sunset times
        val sysData = jsonToMap(allData?.get("sys").toString())
        val sunrise = sysData?.get("sunrise").toString().toDouble().toLong()
        val sunset = sysData?.get("sunset").toString().toDouble().toLong()

        // Get the sunrise and sunset times in the time zone
        val sunriseTime = getSpecifiedTimeInTimeZone(timeZone, sunrise)
        val sunsetTime = getSpecifiedTimeInTimeZone(timeZone, sunset)

        // Get the wind speed
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
            Text(text = city, fontSize = 20.sp) // Show the city name
            Text(text = time, fontSize = 20.sp) // Show the current time
            val iconurl = "https://openweathermap.org/img/wn/$icon@4x.png" // Get the URL of the weather icon
            AsyncImage(
                model = iconurl, // The URL of the image
                contentDescription = "icon",
                modifier = Modifier.size(300.dp)
            ) // Show the weather icon
            val temp = mainData?.get("temp").toString().toDouble() - 273.15 // Get the temperature in Celsius
            Text(text = String.format("%.2f", temp) + "°C", fontSize = 40.sp) // Show the temperature
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
                        painter = painterResource(id = R.drawable.sunrise), // The image of the sunrise
                        contentDescription = "sunrise",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = "SUNRISE")
                    Text(text = sunriseTime) // Show the sunrise time
                }
                Column(
                    modifier = Modifier.fillMaxWidth(.3f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunset), // The image of the sunset
                        contentDescription = "sunset",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = "SUNSET") // Show the text "SUNSET"
                    Text(text = sunsetTime) // Show the sunset time
                }
                Column(
                    modifier = Modifier.fillMaxWidth(.3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.wind), // The image of the wind
                        contentDescription = "wind",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = "WIND")
                    Text(text = String.format("%.2f", windSpeed)+" KM/H") // Show the wind speed
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


// A function to convert a JSON string to a map.
fun jsonToMap(json: String): Map<*, *>? {
    // Create a Gson object.
    val gson = Gson()

    // Return the JSON string deserialized as a map.
    return gson.fromJson(json, Map::class.java)
}

// A function to get the current time in a given timezone.
fun getCurrentTimeInTimeZone(timezoneOffset: Int): String {
    // Create a ZoneOffset object from the timezone offset.
    val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset)

    // Create a ZoneId object from the timezone offset.
    val zoneId = ZoneId.ofOffset("GMT", zoneOffset)

    // Get the current time in the given timezone.
    val zonedDateTime = ZonedDateTime.now(zoneId)

    // Convert the zonedDateTime to a LocalDateTime.
    val localTime = zonedDateTime.toLocalDateTime()

    // Create a DateTimeFormatter object to format the time.
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    // Return the formatted time.
    return localTime.format(formatter)
}

// A function to get the time at a specified timestamp in a given timezone.
fun getSpecifiedTimeInTimeZone(timezoneOffset: Int, timestamp: Long): String {
    // Create a ZoneOffset object from the timezone offset.
    val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset)

    // Create a ZoneId object from the timezone offset.
    val zoneId = ZoneId.ofOffset("GMT", zoneOffset)

    // Create a ZonedDateTime object from the timestamp and timezone.
    val zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), zoneId)

    // Create a DateTimeFormatter object to format the time.
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    // Return the formatted time.
    return zonedDateTime.format(formatter)
}

// A function to extract the icon from a weather forecast string.
fun getIcon(sampleString: String): String? {
    // Create a regular expression pattern to match the icon.
    val pattern = Pattern.compile("icon=([0-9a-z]+)")

    // Create a Matcher object to match the pattern against the input string.
    val matcher = pattern.matcher(sampleString)

    // If the pattern matches, return the icon. Otherwise, return null.
    return if (matcher.find()) {
        matcher.group(1)
    } else {
        null
    }
}

// A function to extract the description from a weather forecast string.
fun getDescription(input: String): String? {
    // Create a regular expression pattern to match the description.
    val regex = """description=(.*?),""".toRegex()

    // Create a MatchResult object to match the pattern against the input string.
    val matchResult = regex.find(input)

    // If the pattern matches, return the description. Otherwise, return null.
    return matchResult?.groups?.get(1)?.value
}

