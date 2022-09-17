package com.example.flights

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flights.ui.screens.Navigation
import com.example.flights.ui.theme.FlightsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlightsTheme {
                Navigation()
            }
        }
    }
}
