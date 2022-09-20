package com.example.flights.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlightsDAO {

    @Insert
    suspend fun insertFlight(flight: Flight)

    @Query("DELETE FROM flights")
    suspend fun deleteAll()

    @Query("SELECT * FROM flights")
    suspend fun getAll(): List<Flight>
}