package com.air_wheelly.wheelly.presentation.reservations

import CarViewModel
import CarViewModelFactory
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.model.CarReservationModel
import com.air_wheelly.wheelly.presentation.components.Base64Image
import com.air_wheelly.wheelly.presentation.components.DatePicker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import hr.air_wheelly.core.network.CarListResponse
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CarReservationScreen(
    navController: NavController,
    carId: String?
) {
    val carViewModel: CarViewModel = viewModel(factory = CarViewModelFactory(LocalContext.current))
    var car by remember { mutableStateOf<CarListResponse?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(carId) {
        carId?.let {
            carViewModel.getCarDetails(it) { fetchedCar ->
                car = fetchedCar
                println("Car fetched: $car")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Reserve Car", style = MaterialTheme.typography.headlineMedium)

        car?.let {
            it.carListingPictures?.firstOrNull()?.image?.let { imageBase64 ->
                Base64Image(
                    base64String = imageBase64,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(bottom = 16.dp)
                )
            }

            Text(text = "Model: ${it.model?.name}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Fuel Type: ${it.fuelType}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Seats: ${it.numberOfSeats}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Location: ${it.location?.adress ?: "Unknown"}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Year: ${it.yearOfProduction}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Rental Price: $${it.rentalPriceType}/day", style = MaterialTheme.typography.bodyLarge)

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
                val rentalPricePerDay = it.rentalPriceType?.toDouble() ?: 0.0
                val reservationModel = CarReservationModel(
                    startDate = startDate.value!!,
                    endDate = endDate.value!!,
                    rentalPricePerDay = rentalPricePerDay
                )
                totalPrice = reservationModel.calculateTotalPrice()
                Text(text = "Total Price: $$totalPrice", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (startDate.value != null && endDate.value != null) {
                    val reservationModel = CarReservationModel(
                        startDate = startDate.value!!,
                        endDate = endDate.value!!,
                        rentalPricePerDay = it.rentalPriceType?.toDouble() ?: 0.0
                    )
                    coroutineScope.launch {
                        val result = reservationModel.createReservation(context, it.id ?: return@launch)
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
            }) {
                Text("Reserve Car")
            }

            Spacer(modifier = Modifier.height(16.dp))

            it.location?.let { location ->
                val cameraPositionState = rememberCameraPositionState {
                    position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
                        com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude), 10f
                    )
                }

                val markerState = rememberMarkerState(
                    position = com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude)
                )

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = markerState,
                        title = location.adress
                    )
                }
            }
        }
    }
}