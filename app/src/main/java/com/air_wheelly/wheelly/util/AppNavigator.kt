package com.air_wheelly.wheelly.util

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.air_wheelly.wheelly.presentation.auth.LoginScreen
import com.air_wheelly.wheelly.presentation.auth.RegisterScreen
import com.air_wheelly.wheelly.presentation.car_listing.CarListingScreen

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("registration") {
            RegisterScreen(navController)
        }
        composable(route = "CarListing") {
            CarListingScreen(navController)
        }
    }
}