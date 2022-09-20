package com.example.flights.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flights.vm.MainViewModel

@Composable
fun Navigation(viewModel: MainViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(route = Screen.Home.route){
            HomeScreen(navController, viewModel)
        }
        composable(route = Screen.Detail.route){
            DetailScreen(navController, viewModel)
        }
    }

}