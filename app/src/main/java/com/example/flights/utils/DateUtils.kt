package com.example.flights.utils


import android.icu.util.TimeZone
import android.util.Log
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


object DateUtils {

    fun getDate(value: Int, countryCode: String, city: String): String{

        val zoneId = ZoneId.of(getZoneId(countryCode, city))
        val instant = Instant.ofEpochSecond(value.toLong())
        val formatter = DateTimeFormatter.ofPattern("dd/MM")

        return instant.atZone(zoneId).format(formatter)

        //return SimpleDateFormat("dd/MM").format((value+7200).toLong()*1000)
    }

    fun getHour(value: Int, countryCode: String, city: String): String{

        val zoneId = ZoneId.of(getZoneId(countryCode, city))
        val instant = Instant.ofEpochSecond(value.toLong())
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        return instant.atZone(zoneId).format(formatter)

        //return SimpleDateFormat("HH:mm").format((value+7200).toLong()*1000)
    }

    private fun getZoneId(countryCode: String, city: String) : String{
        val timezones = TimeZone.getAvailableIDs(countryCode)
        var zone = ""

        if (timezones.size == 1){
            zone = timezones[0]
        } else {
            timezones.forEach {
                if (it.contains(city)){
                    zone = it
                }
            }

            if (zone == ""){
                zone = timezones[0]
            }
        }

        return zone
    }


}