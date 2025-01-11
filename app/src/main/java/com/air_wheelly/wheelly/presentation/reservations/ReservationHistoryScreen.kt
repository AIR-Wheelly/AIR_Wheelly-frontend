package com.air_wheelly.wheelly.presentation.reservations

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse
import hr.air_wheelly.ws.request_handlers.PastReservationsRequestHandler

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
}