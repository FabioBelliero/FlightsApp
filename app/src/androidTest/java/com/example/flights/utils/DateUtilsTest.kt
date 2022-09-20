package com.example.flights.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class DateUtilsTest {

    private var currentTime : Int = 0

    @Before
    fun setup(){
        val date = LocalDateTime.now()
        val zoneId = ZoneId.systemDefault()
        currentTime = date.atZone(zoneId).toEpochSecond().toInt()
    }

    @Test
    fun getDateTest(){
        val result = DateUtils.getDate(currentTime, "IT", "Milan")

        assertThat(result).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM")))
    }

    @Test
    fun getHourTest(){
        val result = DateUtils.getHour(currentTime, "IT", "Milan")

        assertThat(result).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
    }


}