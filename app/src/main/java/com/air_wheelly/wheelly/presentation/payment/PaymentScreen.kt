package com.air_wheelly.wheelly.presentation.payment

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInRequest
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse

@Composable
fun PaymentScreen(
    navController: NavController,
    dropInClient: DropInClient?,
    reservation: PastReservationsResponse?,
    onPurchaseInit: (String, Float) -> Unit
) {
    val context = LocalContext.current
    val dropInRequest = DropInRequest()

    if (reservation != null) {
        onPurchaseInit(reservation.id, reservation.totalPrice)
    }

    fun payForCarRent() {
        Toast.makeText(context, "Start Process", Toast.LENGTH_SHORT).show()
        if (dropInClient != null) {
            dropInClient.launchDropIn(dropInRequest)
        }
    }

    /*val handler = CarByIdRequestHandler(LocalContext.current, reservation!!.carListingId)
    Log.d("ASDFWERCS", reservation.carListingId)

    var carListing by remember { mutableStateOf<CarListResponse?>(null) }

    handler.sendRequest(
        object : ResponseListener<CarListResponse> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<CarListResponse>) {
                carListing = response.result
                Log.d("CARLISTID", carListing.toString())
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.d("CARLISTID", "error")
            }

            override fun onNetworkFailure(t: Throwable) {
                Log.d("CARLISTID", t.cause.toString())
            }
        }
    )*/

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Start Date: ${reservation?.startDate}")
                            Text("End Date: ${reservation?.endDate}")
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Amount to pay: ${reservation?.totalPrice}")
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = { payForCarRent() }
                ) {
                    Text("Pay")
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