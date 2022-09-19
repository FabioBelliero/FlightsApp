package com.example.flights.data

import android.util.Log
import com.example.flights.data.local.Flight
import com.example.flights.data.local.FlightsDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.time.LocalDate
import kotlin.random.Random

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

    override fun onSuccess(flightList: JSONArray){
        val validFlights = mutableListOf<Flight>()

        while (validFlights.size < 5 && flightList.length() > 0) {
            val rand = Random.nextInt(flightList.length())
            val randFlight = flightList.getJSONObject(rand)

            if (ids.contains(randFlight.getString("id"))) {
                flightList.remove(rand)
            } else {
                var f = Flight(
                    randFlight.getString("id"),
                    randFlight.getString("flyFrom"),
                    randFlight.getString("flyTo"),
                    randFlight.getString("cityFrom"),
                    randFlight.getString("cityTo"),
                    randFlight.getJSONObject("countryFrom").getString("name"),
                    randFlight.getJSONObject("countryTo").getString("name"),
                    randFlight.getInt("dTimeUTC"),
                    randFlight.getInt("aTimeUTC"),
                    randFlight.getDouble("distance"),
                    randFlight.getString("fly_duration"),
                    randFlight.getDouble("price"),
                    randFlight.getJSONObject("availability").getInt("seats"),
                    randFlight.getJSONArray("airlines").getString(0),
                    randFlight.getString("deep_link")
                )
                flightList.remove(rand)

                validFlights.add(f)
            }
        }

        _stateFlow.value = validFlights

        newFlightsInDB(validFlights)

    }

    private fun newFlightsInDB(flights: List<Flight>) = runBlocking {
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