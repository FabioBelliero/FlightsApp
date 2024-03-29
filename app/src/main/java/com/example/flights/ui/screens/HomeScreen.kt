package com.example.flights.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flights.data.local.Flight
import com.example.flights.utils.DateUtils
import com.example.flights.vm.MainViewModel

/**
 * Composable function for the home screen
 *
 * It contains references to the main ViewModel and the NavHostController
 * respectively to get the state of the flights and to navigate
 */

private lateinit var vm: MainViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    vm = viewModel
    
    vm.getFlights()
    
    HomeContent(navController)
}

@Composable
fun HomeContent(nav: NavHostController){
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Today's deals!")}
            )
        },
    ) { paddingValues ->
        LazyColumn(modifier = Modifier
            .padding(paddingValues)
        ){
            items(
                state.flightList
            ){
                HomeCard(flight = it, nav)
            }
            
        }
    }
}

@Composable
fun HomeCard(flight: Flight, nav: NavHostController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                vm.selected = flight
                nav.navigate(Screen.Detail.route)
            },
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = flight.flyFrom, fontWeight = FontWeight.Bold)
                Text(text = DateUtils.getHour(flight.departureTime, flight.countryCodeFrom, flight.cityFrom))
                Text(text = DateUtils.getDate(flight.departureTime, flight.countryCodeFrom, flight.cityFrom))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (flight.route.airlines.size == 1){
                        Text(text = "- ", fontWeight = FontWeight.Bold)
                    } else {
                        Text(text = "- ${flight.route.airlines.size} ", fontWeight = FontWeight.Bold)
                    }

                    val angle = 90
                    Icon(
                        imageVector = Icons.Default.AirplanemodeActive,
                        contentDescription = null,
                        modifier = Modifier.rotate(angle.toFloat())
                    )

                    Text(text = " ->", fontWeight = FontWeight.Bold)

                }

                Text(text = flight.duration)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = flight.flyTo, fontWeight = FontWeight.Bold)
                Text(text = DateUtils.getHour(flight.arrivalTime, flight.countryCodeTo, flight.cityTo))
                Text(text = DateUtils.getDate(flight.arrivalTime, flight.countryCodeTo, flight.cityTo))
            }
            Text(
                text = "${flight.price.toInt()}€",
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}