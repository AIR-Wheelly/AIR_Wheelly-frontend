package com.air_wheelly.wheelly.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.air_wheelly.wheelly.presentation.CarListingScreen
import com.air_wheelly.wheelly.presentation.auth.LoginScreen
import com.air_wheelly.wheelly.presentation.auth.RegisterScreen
import com.air_wheelly.wheelly.presentation.car_list.CarList
import com.air_wheelly.wheelly.presentation.payment.PaymentScreen
import com.air_wheelly.wheelly.presentation.profile.ProfileEditScreen
import com.braintreepayments.api.DropInClient
import hr.air_wheelly.ws.models.responses.ProfileResponse

@Composable
fun AppNavigator(
    navController: NavHostController,
    user: ProfileResponse?,
    errorMessage: String?,
    dropInClient: DropInClient? = null,
    onLoginSuccess: (ProfileResponse) -> Unit
) {
    NavHost(navController = navController, startDestination = if (user == null) "paymentScreen" else "carList") {
        composable("login") {
            LoginScreen(navController, onLoginSuccess)
        }
        composable("registration") {
            RegisterScreen(navController)
        }
        composable(route = "profile") {
            ProfileEditScreen(navController)
        }
        composable(route = "createListing"){
            user?.let {
                CarListingScreen(navController, it)
            } ?: run {
                errorMessage?.let {
                    Text(text = "Unexpected error!", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        composable(route = "carList") {
            CarList(navController)
        }
        composable(route = "paymentScreen") {
            PaymentScreen(navController, dropInClient)
        }
    }
}