package com.kaushik.weatherapp

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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