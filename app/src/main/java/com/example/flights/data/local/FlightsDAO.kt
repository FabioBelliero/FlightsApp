package com.example.flights.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FlightsDAO {

    @Query("SELECT EXISTS (SELECT 1 FROM flights WHERE id = :id)")
    fun exists(id: String): Boolean

    @Insert
    suspend fun insertFlight(flight: Flight)

    @Query("DELETE FROM flights")
    fun deleteAll()

    @Query("SELECT * FROM flights")
    fun getAll(): List<Flight>
}