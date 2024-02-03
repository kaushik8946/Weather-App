package com.kaushik.weatherapp

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getSpecifiedTimeInTimeZone(timezoneOffset: Int, timestamp: Long): String {
    val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset)
    val zoneId = ZoneId.ofOffset("GMT", zoneOffset)
    val zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), zoneId)
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return zonedDateTime.format(formatter)
}

fun getCurrentTimeInTimeZone(timezoneOffset: Int): String {
    val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset)
    val zoneId = ZoneId.ofOffset("GMT", zoneOffset)
    val zonedDateTime = ZonedDateTime.now(zoneId)
    val localTime = zonedDateTime.toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return localTime.format(formatter)
}