package com.example.flights.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters( value = [Converter::class] )
@Database(entities = [Flight::class], version = 1, exportSchema = false)
abstract class FlightsDB : RoomDatabase(){
    abstract fun flightDao(): FlightsDAO

    companion object{

        @Volatile
        private var INSTANCE: FlightsDB? = null

        fun getDB(context: Context): FlightsDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlightsDB::class.java,
                    "flights_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}