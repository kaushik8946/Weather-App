package com.kaushik.weatherapp

interface Destinations {
    val route: String // The route of the destination.
    val icon: Int // The icon of the destination.
    val title: String // The title of the destination.
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
