package com.air_wheelly.wheelly.presentation.reservations

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.air_wheelly.wheelly.presentation.components.ReservationCard
import com.google.gson.Gson
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse
import hr.air_wheelly.ws.request_handlers.PastReservationsRequestHandler
import hr.air_wheelly.ws.request_handlers.RenterReservationsRequestHandler

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReservationHistoryScreen(
    navController: NavController
) {
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("User", "Renter")
    var pastReservations by remember { mutableStateOf<List<PastReservationsResponse>?>(null) }

    LaunchedEffect(selectedTabIndex) {
        val handler = if (selectedTabIndex == 0) {
            PastReservationsRequestHandler(context)
        } else {
            RenterReservationsRequestHandler(context)
        }

        handler.sendRequest(
            object : ResponseListener<List<PastReservationsResponse>> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<List<PastReservationsResponse>>) {
                    pastReservations = response.result
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    Log.d("ERROR", "Failed to fetch reservations")
                }

                override fun onNetworkFailure(t: Throwable) {
                    Log.d("NETWORK_ERROR", t.cause.toString())
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        // Reservations list
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val gson = Gson()
                    pastReservations?.forEach { reservation ->
                        if (selectedTabIndex == 0) {
                            ReservationCard(
                                reservation = reservation,
                                onClick = { clickedReservation ->
                                    val reservationJson = gson.toJson(clickedReservation)
                                    navController.navigate("paymentScreen/$reservationJson")
                                },
                                onLongClick = { longClickedReservation ->
                                    navController.navigate("chatScreen/${longClickedReservation.id}")
                                }
                            )
                        } else if (selectedTabIndex == 1) {
                            ReservationCard(
                                reservation = reservation,
                                onClick = { clickedReservation ->
                                    navController.navigate("chatScreen/${clickedReservation.id}")
                                },
                                onLongClick = {}
                            )
                        }
                    }
                }
            }
        }

        com.air_wheelly.wheelly.presentation.components.BottomNavigation(
            navController = navController,
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxWidth()
        )
    }
}