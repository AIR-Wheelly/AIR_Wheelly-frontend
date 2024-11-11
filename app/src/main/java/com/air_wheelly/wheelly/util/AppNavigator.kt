package com.air_wheelly.wheelly.util

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.air_wheelly.wheelly.data.repository.AuthRepositoryImpl
import com.air_wheelly.wheelly.presentation.auth.LoginScreen
import com.air_wheelly.wheelly.presentation.auth.RegisterScreen

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val authService = RetrofitInstance.authService
    val authRepository = AuthRepositoryImpl(authService)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("registration") {
            RegisterScreen(authRepository, navController)
        }
    }
}