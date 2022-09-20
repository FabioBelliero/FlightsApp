package com.example.flights

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flights.data.ApiDataSource
import com.example.flights.data.FlightRepository
import com.example.flights.data.local.FlightsDB
import com.example.flights.ui.screens.Navigation
import com.example.flights.ui.theme.FlightsTheme
import com.example.flights.vm.MainViewModel
import java.time.LocalDate

/**
 * MainActivity
 *
 * It retrieves the last saved date in a shared preference and passes it to the ViewModel
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences : SharedPreferences = this.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val savedDate = sharedPreferences.getString("date", "")

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("date", LocalDate.now().toString())
        editor.apply()

        val viewModel = MainViewModel(
            FlightRepository(
                FlightsDB.getDB(this).flightDao(),
                ApiDataSource(this),
                savedDate.toString()
            )
        )

        setContent {
            FlightsTheme {
                Navigation(viewModel)
            }
        }
    }
}
