package com.example.flights.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


class ApiDataSource(private val appContext: Application) {

    private val url = "https://api.skypicker.com/flights?v=3&sort=popularity&asc=0&locale=en&daysInDestinationFrom=&daysInDestinationTo=&affilid=&children=0&infants=0&flyFrom=49.2-16.61-250km&to=anywhere&featureName=aggregateResults&dateFrom=06/10/2022&dateTo=06/11/2022&typeFlight=oneway&returnFrom=&returnTo=&one_per_date=0&oneforcity=1&wait_for_refresh=0&adults=1&limit=45&partner=skypicker-android"

    fun getFlightsList(callback: VolleyCallback) {

        val queue = Volley.newRequestQueue(appContext)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.d("Volley", "API Responding")

                callback.onSuccess(response.getJSONArray("data"))
            },
            { error ->
                Log.d("Volley", "Error: $error")
                callback.onError()
            }
        )

        queue.add(jsonObjectRequest)
    }

}

//Interface to get the result from the volley call
interface VolleyCallback {
    fun onSuccess(flightList: JSONArray)
    fun onError()
}