package com.kaushik.weatherapp.screens

import com.kaushik.weatherapp.R

interface Destinations {
    val route: String
    val icon: Int
    val title: String
}

object Search : Destinations {
    override val route = "search"
    override val icon = R.drawable.search
    override val title = "Search"
}

object History : Destinations {
    override val route = "history"
    override val icon = R.drawable.clock
    override val title = "History"
}

object Result : Destinations {
    override val route = "result"
    override val icon = R.drawable.weather
    override val title = "Result"
}
