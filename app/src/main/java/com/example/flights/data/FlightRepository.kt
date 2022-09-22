package com.example.flights.data

import android.util.Log
import com.example.flights.data.local.Airlines
import com.example.flights.data.local.Flight
import com.example.flights.data.local.FlightsDAO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.time.LocalDate
import kotlin.random.Random

/**
 * Repository class
 *
 * It's connected to the Room Database and to the remote data source.
 * It checks if it's a new date:
 *
 * if it is it calls the remote data source, gets the new flights and selects them  at random,
 * checks them and when ready it sets the value of the StateFlow and updates the database;
 *
 * if it's the same day it retrieves the latest saved flights from the db and sets the
 * value of the StateFlow.
 */

class FlightRepository(
    private val dao: FlightsDAO,
    private val apiSource: ApiDataSource,
    private val date: String
) : VolleyCallback{

    private var ids = mutableListOf<String>()

    private val _stateFlow = MutableStateFlow(emptyList<Flight>())
    val flightsStateFlow = _stateFlow.asStateFlow()

    suspend fun checkIfNewDay(){

        getIds(dao.getAll())

        if (ids.isEmpty() || LocalDate.now().toString() != date){
            Log.d("FlightRepository", "New day")
            apiSource.getFlightsList(this)
        } else{
            Log.d("FlightRepository", "Same day")
            getCurrentFlights()
        }
    }

    private fun getIds(flights: List<Flight>) {
        flights.forEach{
            ids.add(it.id)
        }
    }

    override fun onSuccess(flightList: JSONArray) = runBlocking {
        getValidFlights(flightList)
    }

    private suspend fun getValidFlights(flightList: JSONArray){
        val validFlights = mutableListOf<Flight>()
        var rand: Int

        while (validFlights.size < 5 && flightList.length() > 0) {
            rand = Random.nextInt(0, flightList.length())
            val randFlight = flightList.getJSONObject(rand)

            if (ids.contains(randFlight.getString("id"))) {
                flightList.remove(rand)
            } else {
                //Branch change
                val airlines = mutableListOf<String>()
                var destination = ""
                val route = randFlight.getJSONArray("route")
                for (i in 0 until route.length()){
                    airlines.add(route.getJSONObject(i).getString("airline"))
                    if (i == route.length()-1){
                        destination = route.getJSONObject(i).getString("mapIdto")
                    }
                }

                val f = Flight(
                    randFlight.getString("id"),
                    randFlight.getString("flyFrom"),
                    randFlight.getString("flyTo"),
                    randFlight.getString("cityFrom"),
                    randFlight.getString("cityTo"),
                    randFlight.getJSONObject("countryFrom").getString("name"),
                    randFlight.getJSONObject("countryTo").getString("name"),
                    randFlight.getJSONObject("countryFrom").getString("code"),
                    randFlight.getJSONObject("countryTo").getString("code"),
                    randFlight.getInt("dTimeUTC"),
                    randFlight.getInt("aTimeUTC"),
                    randFlight.getDouble("distance"),
                    randFlight.getString("fly_duration"),
                    randFlight.getDouble("price"),
                    randFlight.getJSONObject("availability").optInt("seats", 0),

                    //Branch change
                    Airlines(airlines),
                    destination,

                    randFlight.getString("deep_link")
                )
                flightList.remove(rand)
                Log.d("FlightRepository", "New flight: $f")
                validFlights.add(f)
            }
        }

        Log.d("FlightRepository", "Research done")
        _stateFlow.value = validFlights

        newFlightsInDB(validFlights)
    }

    private suspend fun newFlightsInDB(flights: List<Flight>) = coroutineScope {
        launch {
            dao.deleteAll()
            ids = mutableListOf()
            flights.forEach{
                dao.insertFlight(it)
                ids.add(it.id)
            }
        }
    }

    override fun onError() {
        getCurrentFlights()
    }

    private fun getCurrentFlights() = runBlocking {
        launch {
            val flights = dao.getAll()
            _stateFlow.value = flights
        }
    }

}



//Interface to get the result from the volley call
interface VolleyCallback {
    fun onSuccess(flightList: JSONArray)
    fun onError()
}