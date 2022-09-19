package com.example.flights.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtils {

    fun intToDate(value: Int): String{
        val instant = Instant.ofEpochSecond(value.toLong())
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        return instant.atZone(ZoneId.systemDefault().normalized()).format(formatter)
    }


}