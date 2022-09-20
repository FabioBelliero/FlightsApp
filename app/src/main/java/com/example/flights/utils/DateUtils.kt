package com.example.flights.utils


import java.text.SimpleDateFormat


object DateUtils {

    fun getDate(value: Int): String{

        return SimpleDateFormat("dd/MM").format((value+7200).toLong()*1000)
    }

    fun getHour(value: Int): String{

        return SimpleDateFormat("HH:mm").format((value+7200).toLong()*1000)
    }


}