package com.air_wheelly.wheelly.presentation.reservations

import CarViewModel
import CarViewModelFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun CarReservationScreen(
    navController: NavController,
    carId: String?
) {
    val carViewModel: CarViewModel = viewModel(factory = CarViewModelFactory(LocalContext.current))
    val car by remember { mutableStateOf<Car?>(null) }
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

            // Calendar for selecting rental dates
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
                // Handle reservation logic
                if (startDate.value != null && endDate.value != null) {
                    // Proceed with reservation
                } else {
                    Toast.makeText(context, "Please select both start and end dates", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Reserve Car")
            }
        }
    }
}