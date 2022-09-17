package com.example.flights.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
class Flight(
    @PrimaryKey var id: String,
    @ColumnInfo var date: String,

    @ColumnInfo var flyFrom: String,
    @ColumnInfo var flyTo: String,
    @ColumnInfo var cityFrom: String,
    @ColumnInfo var cityTo: String,
    @ColumnInfo var countryFrom: String,
    @ColumnInfo var countryTo: String,

    @ColumnInfo var departureTime: Int,
    @ColumnInfo var arrivalTime: Int,
    @ColumnInfo var distance: Double,
    @ColumnInfo var duration: String,
    @ColumnInfo var price: Double,
    @ColumnInfo var availabilty: Int,
    @ColumnInfo var airline: String,
    @ColumnInfo var link: String,

    @ColumnInfo var bagPrice1: Double,
    @ColumnInfo var bagPrice2: Double,
    @ColumnInfo var handPrice: Double,
    @ColumnInfo var handWidth: Int,
    @ColumnInfo var handHeight: Int,
    @ColumnInfo var handLength: Int,
    @ColumnInfo var handWeight: Int,
    @ColumnInfo var holdWidth: Int,
    @ColumnInfo var holdHeight: Int,
    @ColumnInfo var holdLength: Int,
    @ColumnInfo var holdWeight: Int
){}
