package com.kaushik.weatherapp.api

import okhttp3.OkHttpClient
import okhttp3.Request

object WeatherAPI {

    private val okHttpClient = OkHttpClient()

    fun getResponse(city: String): Pair<Boolean, String> {
        val baseURL = "https://api.openweathermap.org/data/2.5/weather?"

        val url = baseURL + "appid=$API_KEY&q=$city"

        val request = Request.Builder()
            .url(url)
            .build()

        val response = okHttpClient.newCall(request).execute()

        if (response.isSuccessful) {
            val json = response.body?.string()
            return Pair(true, json.toString())
        } else {
            return Pair(false, response.message)
        }
    }
}
