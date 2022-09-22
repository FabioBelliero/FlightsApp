package com.example.flights.data

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray

/**
 * Class to call the API
 *
 * It uses Volley to send a GET request for the JSONObject.
 * If the response is positive it gets the data JSONArray and calls the success
 * callback implemented in the repository, otherwise report the error and gets the
 * saved flights in the Database.
 */

class ApiDataSource(private val context: Context) {

    private val url = "https://api.skypicker.com/flights?v=3&sort=popularity&asc=0&locale=en&daysInDestinationFrom=&daysInDestinationTo=&affilid=&children=0&infants=0&flyFrom=49.2-16.61-250km&to=anywhere&featureName=aggregateResults&dateFrom=06/10/2022&dateTo=06/11/2022&typeFlight=oneway&returnFrom=&returnTo=&one_per_date=0&oneforcity=1&wait_for_refresh=0&adults=1&limit=45&partner=skypicker-android"

    fun getFlightsList(callback: VolleyCallback) {

        val queue = Volley.newRequestQueue(context)

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
