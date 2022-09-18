package com.example.flights.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.flights.data.local.Flight
import com.example.flights.data.local.FlightsDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import java.time.LocalDate
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class FlightRepository(
    private val dao: FlightsDAO,
    private val apiSource: ApiDataSource,
    private val date: String
) : VolleyCallback{

    private val _stateFlow = MutableStateFlow(emptyList<Flight>())
    val flightsStateFlow = _stateFlow.asStateFlow()

    fun checkIfNewDay() {

        if (dao.getAll().isEmpty() || LocalDate.now().toString() != date){
            val flights = apiSource.getFlightsList(this)
        } else{
            getCurrentFlights()
        }
    }

    override fun onSuccess(flightList: JSONArray) {
        val validFlights = mutableListOf<Flight>()

        while (validFlights.size < 5 && flightList.length() > 0) {
            val rand = Random.nextInt(flightList.length())
            val randFlight = flightList.getJSONObject(rand)

            if (dao.exists(randFlight.getJSONObject("id").toString())) {
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
                    randFlight.getString("deep_link"),
                    randFlight.getJSONObject("bag_price").getDouble("1"),
                    randFlight.getJSONObject("bag_price").getDouble("2"),
                    randFlight.getJSONObject("bag_price").getDouble("hand"),
                    randFlight.getJSONObject("baglimit").getInt("hand_width"),
                    randFlight.getJSONObject("baglimit").getInt("hand_height"),
                    randFlight.getJSONObject("baglimit").getInt("hand_length"),
                    randFlight.getJSONObject("baglimit").getInt("hand_weight"),
                    randFlight.getJSONObject("baglimit").getInt("hold_width"),
                    randFlight.getJSONObject("baglimit").getInt("hold_height"),
                    randFlight.getJSONObject("baglimit").getInt("hold_length"),
                    randFlight.getJSONObject("baglimit").getInt("hold_weight")
                )

                validFlights.add(f)
            }
        }

        _stateFlow.value = validFlights

        dao.deleteAll()
        validFlights.forEach{
            insertFlightInDB(it)
        }
    }

    private fun insertFlightInDB(flight: Flight) = runBlocking {
        launch { dao.insertFlight(flight) }
    }

    override fun onError() {
        getCurrentFlights()
    }

    private fun getCurrentFlights(){
        val flights = dao.getAll()
        _stateFlow.value = flights
    }

}