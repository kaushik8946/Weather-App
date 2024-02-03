package com.kaushik.weatherapp

import com.google.gson.Gson
import java.util.regex.Pattern


// A function to convert a JSON string to a map.
fun jsonToMap(json: String): Map<*, *>? {
    // Create a Gson object.
    val gson = Gson()

    // Return the JSON string deserialized as a map.
    return gson.fromJson(json, Map::class.java)
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
