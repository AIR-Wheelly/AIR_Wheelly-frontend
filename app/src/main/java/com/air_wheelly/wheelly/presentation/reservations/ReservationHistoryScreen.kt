package com.air_wheelly.wheelly.presentation.reservations

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.air_wheelly.wheelly.presentation.components.ReservationCard
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse
import hr.air_wheelly.ws.request_handlers.PastReservationsRequestHandler

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReservationHistoryScreen(
    navController: NavController
) {
    val handler = PastReservationsRequestHandler(LocalContext.current)

    var pastReservations by remember { mutableStateOf<List<PastReservationsResponse>?>(null) }

    handler.sendRequest(
        object : ResponseListener<List<PastReservationsResponse>> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<List<PastReservationsResponse>>) {
                pastReservations = response.result
                Log.d("ASDFSDFASDFS", pastReservations.toString())
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.d("ERROR", "Nije moguce dohvatiti")
            }

            override fun onNetworkFailure(t: Throwable) {
                Log.d("NETWORKERROR", t.cause.toString())
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ){
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
                    pastReservations?.forEach { reservation ->
                        ReservationCard(reservation)
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