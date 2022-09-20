package com.example.flights.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.flights.data.local.Flight
import com.example.flights.ui.theme.FlightsTheme
import com.example.flights.utils.DateUtils
import com.example.flights.vm.MainViewModel

private lateinit var vm: MainViewModel
private lateinit var nav: NavHostController

@Composable
fun DetailScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    nav = navController

    vm = viewModel

    DetailContent()

}

@Composable
fun DetailContent(){
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
                Row() {
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
                    modifier = Modifier.fillMaxWidth().padding(15.dp)
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
                .padding(15.dp),
            horizontalAlignment = Alignment.Start
        ) {
            textWithIcon(text = " From: ", "${flight.cityFrom} (${flight.countryFrom}) - ${flight.flyFrom}",
                icon = Icons.Default.FlightTakeoff)
            textWithIcon(text = " To: ", "${flight.cityTo} (${flight.countryTo}) - ${flight.flyTo}",
                icon = Icons.Default.FlightLand)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                thickness = 1.dp
            )

            textWithIcon(text = " Departure: ","${DateUtils.getHour(flight.departureTime, flight.countryCodeFrom, flight.cityFrom)} ${DateUtils.getDate(flight.departureTime, flight.countryCodeFrom, flight.cityFrom)}",
                icon = Icons.Default.Today)
            textWithIcon(text = " Arrival: ", "${DateUtils.getHour(flight.arrivalTime, flight.countryCodeTo, flight.cityTo)} ${DateUtils.getDate(flight.arrivalTime, flight.countryCodeTo, flight.cityTo)}",
                icon = Icons.Default.Event)
            textWithIcon(text = " Flights to destination: ", "${flight.route}",
                icon = Icons.Default.ConnectingAirports)
            textWithIcon(text = " Flight duration: ", flight.duration,
                icon = Icons.Default.Schedule)
            textWithIcon(text = " Distance traveled: ", "${flight.distance}km",
                icon = Icons.Default.Moving)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                thickness = 1.dp
            )

            textWithIcon(text = " Seats available: ", "${flight.availabilty}",
                icon = Icons.Default.AirlineSeatReclineNormal)

            Text(
                text = "${flight.price.toInt()}€",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.h3.fontSize
            )


        }

        /*
        Row(
            modifier = Modifier
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(text = DateUtils.getHour(flight.departureTime))
                Text(text = DateUtils.getDate(flight.departureTime))
                Text(text = flight.duration)
                Text(text = DateUtils.getHour(flight.arrivalTime))
                Text(text = DateUtils.getDate(flight.arrivalTime))
            }
            Column() {
                val angle = 180
                Icon(imageVector = Icons.Default.FlightTakeoff, contentDescription = null)
                Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = null)
                Icon(
                    imageVector = Icons.Default.AirplanemodeActive,
                    contentDescription = null,
                    modifier = Modifier.rotate(angle.toFloat())
                )
                Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = null)
                Icon(imageVector = Icons.Default.FlightLand, contentDescription = null)
            }
            Column() {
                Text(text = "${flight.cityFrom} (${flight.countryFrom})")
                Text(text = flight.flyFrom)
                Text(text = "${flight.route} flight")
                Text(text = "${flight.cityTo} (${flight.countryTo})")
                Text(text = flight.flyTo)
            }
        }

         */
    }
}

@Composable
fun textWithIcon(text: String, content: String, icon: ImageVector){
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

@Composable
@Preview(showBackground = true, name = "light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "dark")
fun DetailScreenPreview(){
    FlightsTheme {
        DetailContent()
    }
}