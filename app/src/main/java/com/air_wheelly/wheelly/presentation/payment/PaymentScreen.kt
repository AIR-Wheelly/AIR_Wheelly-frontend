package com.air_wheelly.wheelly.presentation.payment

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.air_wheelly.wheelly.presentation.components.ReviewPopup
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
            } else if (state.review) {
                ReviewPopup(
                    onConfirm = { rating ->
                        viewModel.updateReviewState(false)
                        viewModel.submitReview(rating, reservation!!.carListingId)
                    },
                    onDismiss = { viewModel.updateReviewState(false) }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    state.carListingById?.carListingPictures?.firstOrNull()?.image?.let { imageBase64 ->
                        Base64Image(
                            imageBase64,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                    ) {

                        Text(
                            text = state.carListingById?.model?.name ?: "Unknown Model",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "${state.carListingById?.yearOfProduction} | ${state.carListingById?.fuelType} | ${state.carListingById?.numberOfSeats} Seats",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        state.carListingById?.location?.let { location ->
                            val locationText = "Show on map"
                            val locationUri = "geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}(${location.adress})"

                            Text(
                                text = locationText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUri))
                                    intent.setPackage("com.google.android.apps.maps")
                                    context.startActivity(intent)
                                }
                            )
                        } ?: Text(
                            text = "Location: Unknown",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Rental Price: $${state.carListingById?.rentalPriceType}/day",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 16.dp, end = 16.dp),
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
                                .padding(top = 16.dp),
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

                        if (!reservation!!.isPaid && !state.isPayed) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 75.dp),
                                onClick = { viewModel.updateReviewState(true) }
                            ) {
                                Text("Leave a review")
                            }
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                onClick = { payForCarRent() }
                            ) {
                                Text("Pay")
                            }
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