package com.example.flights.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flights.data.FlightRepository
import com.example.flights.data.local.Airlines
import com.example.flights.data.local.Flight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    var selected: Flight = Flight("0",  "", "", "", "", "", "", "", "", 0, 0, 0.0, "", 0.0, 0, Airlines(
        emptyList()), "")

    private val _state = MutableStateFlow(HomeScreenState())
    val state : StateFlow<HomeScreenState>
        get() = _state

    init {
        viewModelScope.launch {
            repository.flightsStateFlow.collectLatest {
                Log.d("MainViewModel", "List updated")
                _state.value = HomeScreenState(it)
            }
        }
    }

    fun getFlights() {
        if(_state.value.flightList.isEmpty()) {
            viewModelScope.launch {
                Log.d("MainViewModel", "Check if new day")
                repository.checkIfNewDay()
            }
        }
    }

}

data class HomeScreenState(
    val flightList: List<Flight> = emptyList()
)