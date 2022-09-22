package com.example.flights.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flights.data.FlightRepository
import com.example.flights.data.local.Flight
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

/**
 * Main ViewModel
 *
 * This class stores data for the UI and calls the repository
 * to get the flights data.
 * It receives a StateFlow from the repository and presents a
 * HomeScreenState to the UI
 */

class MainViewModel(
    private val repository: FlightRepository
) : ViewModel(){

    lateinit var selected: Flight

    private val _state = MutableStateFlow(HomeScreenState())
    val state : StateFlow<HomeScreenState>
        get() = _state

    var midnight = false
    var check = false

    init {
        viewModelScope.launch {
            repository.flightsStateFlow.collectLatest {
                Log.d("MainViewModel", "List updated")
                check = false
                _state.value = HomeScreenState(it)
            }
        }
        //reset at midnight
        midnightReset()
    }

    fun getFlights() {
        if(_state.value.flightList.isEmpty() && !check) {
            check = true
            viewModelScope.launch {
                Log.d("MainViewModel", "Check if new day")
                repository.checkIfNewDay()
            }
        }
    }

    /**
     * Simplest solution to know when midnight is:
     * 1) Calculate midnight using calendar;
     * 2) Calculate the milliseconds until midnight
     * 3) Delay until that many millis
     */
    private fun midnightReset() = viewModelScope.launch {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        delay(calendar.timeInMillis - System.currentTimeMillis())

        Log.d("Midnight", "is midnight")
        midnight = true

        _state.value = HomeScreenState(emptyList())

        getFlights()

    }

}

data class HomeScreenState(
    val flightList: List<Flight> = emptyList()
)

data class MidnightState(
    val isMidnight: Boolean = false
)