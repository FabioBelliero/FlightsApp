package com.example.flights.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flights.data.FlightRepository
import com.example.flights.data.local.Flight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FlightRepository
) : ViewModel(){

    var selected: Flight = Flight("0",  "", "", "", "", "", "", 0, 0, 0.0, "", 0.0, 0, 0, "")

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
        viewModelScope.launch {
            repository.checkIfNewDay()
        }
    }

}

data class HomeScreenState(
    val flightList: List<Flight> = emptyList()
)