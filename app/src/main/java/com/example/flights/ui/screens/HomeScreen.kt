package com.example.flights.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.flights.data.FlightRepository
import com.example.flights.data.local.Flight
import com.example.flights.data.local.FlightsDB
import com.example.flights.ui.theme.FlightsTheme
import com.example.flights.utils.DateUtils
import com.example.flights.vm.MainViewModel

private lateinit var vm: MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {

    vm = viewModel
    
    vm.getFlights()
    
    HomeContent()
}

@Composable
fun HomeContent(){
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
                HomeCard(flight = it)
            }
            
        }
    }
}

@Composable
fun HomeCard(flight: Flight){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                //TODO
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
                Text(text = flight.flyFrom)
                Text(text = DateUtils.intToDate(flight.departureTime))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "->")
                Text(text = flight.duration)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = flight.flyTo)
                Text(text = DateUtils.intToDate(flight.arrivalTime))
            }
            Text(
                text = "${flight.price.toInt()}€",
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
@Preview(showBackground = true, name = "light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "dark")
fun HomeScreenPreview(){
    FlightsTheme {
        HomeContent()
    }
}