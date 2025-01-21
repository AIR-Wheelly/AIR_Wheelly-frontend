package com.air_wheelly.wheelly.presentation.payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.payment.PaymentViewModel
import com.air_wheelly.wheelly.domain.payment.PaymentViewModelFactory
import com.air_wheelly.wheelly.presentation.components.Base64Image
import com.air_wheelly.wheelly.util.LocalDateFormatter
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
    val viewModel: PaymentViewModel = viewModel(factory = PaymentViewModelFactory(context))
    val state by viewModel.state.collectAsState()
    val dropInRequest = DropInRequest()
    val localDateFormatter = LocalDateFormatter()

    if (reservation != null) {
        onPurchaseInit(reservation.id, reservation.totalPrice)
    }

    fun payForCarRent() {
        viewModel.launchPayment(dropInClient, dropInRequest)
    }

    LaunchedEffect(reservation) {
        reservation?.carListingId?.let {
            viewModel.fetchCarDetails(it)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.errorMessage != null) {
                com.air_wheelly.wheelly.presentation.components.AlertDialog(
                    title = "Error",
                    message = state.errorMessage.toString(),
                    onDismiss = { viewModel.clearMessages() }
                )
            } else {
                Column {

                    state.carListingById?.carListingPictures?.firstOrNull()?.image?.let { imageBase64 ->
                        Base64Image(
                            imageBase64, modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .size(300.dp)
                                .padding(top = 16.dp)
                        )
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
                                Text("Start Date: ${reservation?.startDate?.let { localDateFormatter.toLocalDate(it) }}")
                                Text("End Date: ${reservation?.endDate?.let { localDateFormatter.toLocalDate(it) }}")
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
        }
        com.air_wheelly.wheelly.presentation.components.BottomNavigation(
            navController = navController,
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxWidth()
        )
    }
}