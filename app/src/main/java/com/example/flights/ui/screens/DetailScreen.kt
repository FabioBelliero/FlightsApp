package com.example.flights.ui.screens

import android.icu.number.Scale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.flights.data.local.Flight
import com.example.flights.utils.DateUtils
import com.example.flights.vm.MainViewModel

/**
 * Composable function for the detail screen
 *
 * It contains references to the main ViewModel and the NavHostController
 * respectively to get the selected flight and to navigate back
 */

private lateinit var vm: MainViewModel

@Composable
fun DetailScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    vm = viewModel

    DetailContent(navController)

}

@Composable
fun DetailContent(nav: NavHostController){
    val flight = vm.selected

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "${flight.flyFrom} -> ${flight.flyTo}") },
                navigationIcon = {
                    IconButton(onClick = {
                        nav.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row {
                Text(text = "${flight.cityFrom} -> ${flight.cityTo}",
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold)
            }}
            FlightDetailsCard(flight)
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Want to know more?",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.h5.fontSize
                )
                
                val handler = LocalUriHandler.current
                
                Button(
                    onClick = { handler.openUri(flight.link) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ){
                    Text(
                        text = "Visit our website!",
                        fontSize = MaterialTheme.typography.h5.fontSize
                    )
                }
            }

        }

    }
}

@Composable
fun FlightDetailsCard(flight: Flight){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {

            SubcomposeAsyncImage(
                model = "https://images.kiwi.com/photos/600x330/${flight.destinationId}.jpg",
                contentDescription = null,
                loading = { CircularProgressIndicator() },
                modifier = Modifier.fillMaxWidth()
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                thickness = 1.dp
            )

            TextWithIcon(text = " From: ", "${flight.cityFrom} (${flight.countryFrom}) - ${flight.flyFrom}",
                icon = Icons.Default.FlightTakeoff)
            TextWithIcon(text = " To: ", "${flight.cityTo} (${flight.countryTo}) - ${flight.flyTo}",
                icon = Icons.Default.FlightLand)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                thickness = 1.dp
            )

            TextWithIcon(text = " Departure: ","${DateUtils.getHour(flight.departureTime, flight.countryCodeFrom, flight.cityFrom)} ${DateUtils.getDate(flight.departureTime, flight.countryCodeFrom, flight.cityFrom)}",
                icon = Icons.Default.Today)
            TextWithIcon(text = " Arrival: ", "${DateUtils.getHour(flight.arrivalTime, flight.countryCodeTo, flight.cityTo)} ${DateUtils.getDate(flight.arrivalTime, flight.countryCodeTo, flight.cityTo)}",
                icon = Icons.Default.Event)
            TextWithIcon(text = " Flights to destination: ", "${flight.route.airlines.size}",
                icon = Icons.Default.ConnectingAirports)

            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                flight.route.airlines.forEach {
                    SubcomposeAsyncImage(
                        model = "https://images.kiwi.com/airlines/64x64/$it.png",
                        contentDescription = null,
                        loading = { CircularProgressIndicator() },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }

            TextWithIcon(text = " Flight duration: ", flight.duration,
                icon = Icons.Default.Schedule)
            TextWithIcon(text = " Distance traveled: ", "${flight.distance}km",
                icon = Icons.Default.Moving)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                thickness = 1.dp
            )

            TextWithIcon(text = " Seats available: ", "${flight.availabilty}",
                icon = Icons.Default.AirlineSeatReclineNormal)

            Text(
                text = "${flight.price.toInt()}€",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.h3.fontSize
            )


        }
    }
}

@Composable
fun TextWithIcon(text: String, content: String, icon: ImageVector){
    Text(text = buildAnnotatedString {
        appendInlineContent("inLineContent", "[icon]")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
            append(text)
        }
        append(content)
    },
        inlineContent = mapOf(
            Pair("inLineContent", InlineTextContent(
                Placeholder(
                    width = 18.sp,
                    height = 18.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ){
                Icon(imageVector = icon, contentDescription = null)
            })
        )
    )
}
