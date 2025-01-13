package com.air_wheelly.wheelly.presentation.reservations

import CarViewModel
import CarViewModelFactory
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.presentation.components.DatePicker
import hr.air_wheelly.core.network.CarListResponse
import java.time.LocalDate


@Composable
fun CarReservationScreen(
    navController: NavController,
    carId: String?
) {
    val carViewModel: CarViewModel = viewModel(factory = CarViewModelFactory(LocalContext.current))
    var car by remember { mutableStateOf<CarListResponse?>(null) }
    val context = LocalContext.current

    LaunchedEffect(carId) {
        carId?.let {
            carViewModel.getCarDetails(it) { fetchedCar ->
                car = fetchedCar
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        car?.let {
            Text(text = "Model: ${it.model?.name}", style = MaterialTheme.typography.h6)
            Text(text = "Fuel Type: ${it.fuelType}", style = MaterialTheme.typography.body1)
            Text(text = "Seats: ${it.numberOfSeats}", style = MaterialTheme.typography.body1)
            Text(text = "Location: ${it.location}", style = MaterialTheme.typography.body1)
            Text(text = "Year: ${it.yearOfProduction}", style = MaterialTheme.typography.body1)
            Text(text = "Rental Price: $${it.rentalPriceType}/day", style = MaterialTheme.typography.body1)

            Spacer(modifier = Modifier.height(16.dp))

            val startDate = remember { mutableStateOf<LocalDate?>(null) }
            val endDate = remember { mutableStateOf<LocalDate?>(null) }

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

            Button(onClick = {
                if (startDate.value != null && endDate.value != null) {
                } else {
                    Toast.makeText(context, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Reserve Car")
            }
        }
    }
}