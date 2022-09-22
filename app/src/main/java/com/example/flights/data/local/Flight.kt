package com.example.flights.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "flights")
class Flight(
    @PrimaryKey var id: String,

    @ColumnInfo var flyFrom: String,
    @ColumnInfo var flyTo: String,
    @ColumnInfo var cityFrom: String,
    @ColumnInfo var cityTo: String,
    @ColumnInfo var countryFrom: String,
    @ColumnInfo var countryTo: String,
    @ColumnInfo var countryCodeFrom: String,
    @ColumnInfo var countryCodeTo: String,

    @ColumnInfo var departureTime: Int,
    @ColumnInfo var arrivalTime: Int,
    @ColumnInfo var distance: Double,
    @ColumnInfo var duration: String,
    @ColumnInfo var price: Double,
    @ColumnInfo var availabilty: Int?,
    @ColumnInfo var route: Airlines,
    @ColumnInfo var destinationId: String,
    @ColumnInfo var link: String,
){}

data class Airlines(val airlines: List<String>)

class Converter {
    @TypeConverter
    fun fromListToJSON(value: Airlines): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromJSONToList(value: String): Airlines {
        return Gson().fromJson(value, Airlines::class.java)
    }
}
