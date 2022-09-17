package com.example.flights.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FlightsDaoTest {

    private lateinit var db: FlightsDB
    private lateinit var dao: FlightsDAO

    @Before
    fun setup(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FlightsDB::class.java
        ).allowMainThreadQueries().build()

        dao = db.flightDao()
    }

    @After
    fun teardown(){
        db.close()
    }

    @Test
    fun insertFlight() = runTest {
        val flight = Flight("1", "", "", "", "", "", "", "", 0, 0, 0.0, "", 0.0, 0, "", "", 0.0, 0.0, 0.0, 0,0,0,0,0,0,0,0)
        dao.insertFlight(flight)

        val result = dao.exists("1")

        assertThat(result).isTrue()
    }

    @Test
    fun deleteAllElements() = runTest {
        val flight = Flight("1", "", "", "", "", "", "", "", 0, 0, 0.0, "", 0.0, 0, "", "", 0.0, 0.0, 0.0, 0,0,0,0,0,0,0,0)
        dao.insertFlight(flight)

        dao.deleteAll()

        val result = dao.exists("1")
        assertThat(result).isFalse()
    }
}