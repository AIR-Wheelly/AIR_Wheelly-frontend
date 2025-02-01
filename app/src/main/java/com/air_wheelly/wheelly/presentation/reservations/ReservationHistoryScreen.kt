package com.air_wheelly.wheelly.presentation.reservations

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReservationHistoryScreen(
    navController: NavController
) {
    val context = LocalContext.current

    var selectedFilter by remember { mutableStateOf("User") }
    var pastReservations by remember { mutableStateOf<List<PastReservationsResponse>?>(null) }

    LaunchedEffect(selectedFilter) {
        val handler = if (selectedFilter == "User") {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterButton(
                text = "User",
                isSelected = selectedFilter == "User",
                onClick = { selectedFilter = "User" }
            )
            FilterButton(
                text = "Renter",
                isSelected = selectedFilter == "Renter",
                onClick = { selectedFilter = "Renter" }
            )
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
                        if (selectedFilter == "User") {
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
                        } else if (selectedFilter == "Renter") {
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

@Composable
fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    androidx.compose.material.Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp),
        elevation = androidx.compose.material.ButtonDefaults.elevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = androidx.compose.material.ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) androidx.compose.ui.graphics.Color(0xFF6200EE) else androidx.compose.ui.graphics.Color(0xFFE0E0E0),
            contentColor = androidx.compose.ui.graphics.Color.White
        )
    ) {
        Text(
            text = text,
            color = if (isSelected) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black
        )
    }
}