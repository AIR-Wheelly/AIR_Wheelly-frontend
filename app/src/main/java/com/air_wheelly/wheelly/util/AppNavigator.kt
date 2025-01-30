package com.air_wheelly.wheelly.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.air_wheelly.wheelly.presentation.CarListingScreen
import com.air_wheelly.wheelly.presentation.auth.LoginScreen
import com.air_wheelly.wheelly.presentation.auth.RegisterScreen
import com.air_wheelly.wheelly.presentation.car_list.CarList
import com.air_wheelly.wheelly.presentation.chat.ChatScreen
import com.air_wheelly.wheelly.presentation.payment.PaymentScreen
import com.air_wheelly.wheelly.presentation.profile.ProfileScreen
import com.air_wheelly.wheelly.presentation.reservations.CarReservationScreen
import com.air_wheelly.wheelly.presentation.reservations.ReservationHistoryScreen
import com.air_wheelly.wheelly.presentation.statistics.StatisticsScreen
import com.braintreepayments.api.DropInClient
import com.google.gson.Gson
import hr.air_wheelly.ws.models.responses.ProfileResponse
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse

@Composable
fun AppNavigator(
    navController: NavHostController,
    user: ProfileResponse?,
    errorMessage: String?,
    dropInClient: DropInClient,
    onPurchaseInit: (String, Float) -> Unit,
    onLoginSuccess: (ProfileResponse) -> Unit
) {
    val gson = Gson()

    NavHost(navController = navController, startDestination = if (user == null) "login" else "carList") {
        composable("login") {
            LoginScreen(navController, onLoginSuccess)
        }
        composable("registration") {
            RegisterScreen(navController)

        }
        composable(route = "profile") {
            ProfileScreen(navController)
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

        composable("car_reservation/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")
            CarReservationScreen(navController, carId)
        }

        composable(
            route = "paymentScreen/{reservation}",
            arguments = listOf(navArgument("reservation") { type = NavType.StringType })
        ) { backStackEntry ->
            val reservationJson = backStackEntry.arguments?.getString("reservation")
            val reservation = gson.fromJson(reservationJson, PastReservationsResponse::class.java)
            PaymentScreen(navController, dropInClient, reservation, onPurchaseInit)
        }
        composable(route = "history") {
            ReservationHistoryScreen(navController)
        }
        composable(route = "statistics") {
            StatisticsScreen(navController)
        }

        composable(route = "chatScreen/{reservationId}", arguments = listOf(navArgument("reservationId") { type = NavType.StringType })) { backStackEntry ->
            val reservationId = backStackEntry.arguments?.getString("reservationId")
            reservationId?.let {
                user?.let { currentUser ->
                    ChatScreen(reservationId = it, currentUserId = currentUser.id)
                }
            }
        }
    }
}