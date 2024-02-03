package com.kaushik.weatherapp

import com.google.gson.Gson
import java.util.regex.Pattern

fun jsonToMap(json: String): Map<*, *>? {
    val gson = Gson()
    return gson.fromJson(json, Map::class.java)
}

fun getIcon(sampleString: String): String? {
    val pattern = Pattern.compile("icon=([0-9a-z]+)")
    val matcher = pattern.matcher(sampleString)
    return if (matcher.find()) {
        matcher.group(1)
    } else {
        null
    }
}

fun getDescription(input: String): String? {
    val regex = """description=(.*?),""".toRegex()
    val matchResult = regex.find(input)
    return matchResult?.groups?.get(1)?.value
}
