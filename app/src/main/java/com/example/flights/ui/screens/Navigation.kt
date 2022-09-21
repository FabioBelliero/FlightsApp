package com.example.flights.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.example.flights.vm.MainViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(viewModel: MainViewModel){
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController = navController, startDestination = Screen.Home.route){
        composable(
            route = Screen.Home.route
        ){
            HomeScreen(navController, viewModel)
        }
        composable(route = Screen.Detail.route,
            enterTransition = {
                when (targetState.destination.route){ Screen.Detail.route ->
                    slideInHorizontally(
                        initialOffsetX = {300},
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(300))
                    else -> null
                }
            },
            ){
            DetailScreen(navController, viewModel)
        }
    }

}