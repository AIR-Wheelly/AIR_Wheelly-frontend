package com.air_wheelly.wheelly.presentation.reservations

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.CarReservationModel
import com.air_wheelly.wheelly.domain.reservation.CarViewModel
import com.air_wheelly.wheelly.domain.reservation.CarViewModelFactory
import com.air_wheelly.wheelly.presentation.components.DatePicker
import com.air_wheelly.wheelly.presentation.components.StarRating
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import hr.air_wheelly.ws.models.responses.CarListResponse
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.time.LocalDate

@Composable
fun Base64Image(
    base64String: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    base64String?.let {
        val decoded = remember(it) { Base64.decode(it, Base64.DEFAULT) }
        val bitmap = remember(decoded) {
            val stream = ByteArrayInputStream(decoded)
            BitmapFactory.decodeStream(stream)
        }
        bitmap?.let { bmp ->
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
                modifier = modifier,
                contentScale = contentScale
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarImagePager(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        count = images.size,
        state = pagerState,
        modifier = modifier.fillMaxWidth()
    ) { page ->
        Base64Image(
            base64String = images[page],
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarReservationScreen(
    navController: NavController,
    carId: String?
) {
    val carViewModel: CarViewModel = viewModel(factory = CarViewModelFactory(LocalContext.current))
    var car by remember { mutableStateOf<CarListResponse?>(null) }
    val state by carViewModel.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(carId) {
        carId?.let {
            carViewModel.getCarDetails(it) { fetchedCar ->
                car = fetchedCar
            }
        }
    }
    car?.let { carDetails ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            carDetails.carListingPictures?.let { pictures ->
                val images = pictures.mapNotNull { it.image }
                if (images.isNotEmpty()) {
                    CarImagePager(
                        images = images,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = (carDetails.model?.manufacturerName ?: "") + " " + (carDetails.model?.name ?: ""),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "${carDetails.yearOfProduction} | ${carDetails.fuelType} | ${carDetails.numberOfSeats} Seats",
                    style = MaterialTheme.typography.bodyLarge
                )

                carDetails.location?.let { location ->
                    val locationText = "Show on map"
                    val locationUri =
                        "geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}(${location.adress})"

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
                    text = "Price: ${carDetails.rentalPriceType}EUR/day",
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    StarRating(
                        rating = state.averageGrade,
                        starSize = 20.dp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "%.1f".format(state.averageGrade),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val startDate = remember { mutableStateOf<LocalDate?>(null) }
                val endDate = remember { mutableStateOf<LocalDate?>(null) }
                var totalPrice by remember { mutableStateOf(0.0) }

                DatePicker(
                    label = "Select Start Date",
                    selectedDate = startDate.value,
                    onDateSelected = { date -> startDate.value = date }
                )

                Spacer(modifier = Modifier.height(8.dp))

                DatePicker(
                    label = "Select End Date",
                    selectedDate = endDate.value,
                    onDateSelected = { date -> endDate.value = date }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (startDate.value != null && endDate.value != null) {
                    val rentalPricePerDay = carDetails.rentalPriceType?.toDouble() ?: 0.0
                    val reservationModel = CarReservationModel(
                        startDate = startDate.value!!,
                        endDate = endDate.value!!,
                        rentalPricePerDay = rentalPricePerDay
                    )
                    totalPrice = reservationModel.calculateTotalPrice()
                    Text(text = "Total Price: $$totalPrice", style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (startDate.value != null && endDate.value != null) {
                                val reservationModel = CarReservationModel(
                                    startDate = startDate.value!!,
                                    endDate = endDate.value!!,
                                    rentalPricePerDay = carDetails.rentalPriceType?.toDouble() ?: 0.0
                                )
                                coroutineScope.launch {
                                    val result = reservationModel.createReservation(
                                        context,
                                        carDetails.id ?: return@launch
                                    )
                                    if (result.isSuccess) {
                                        Toast.makeText(context, "Reservation successful", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(context, "Failed to create reservation", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Reserve Car")
                    }
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}