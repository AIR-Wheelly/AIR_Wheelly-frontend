package com.air_wheelly.wheelly.presentation.car_listing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListingScreen(
    navController: NavController
) {


    var manufacturer by remember { mutableStateOf(TextFieldValue("")) }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var year by remember { mutableStateOf(TextFieldValue("")) }
    var seats by remember { mutableStateOf(TextFieldValue("")) }
    var fuelType by remember { mutableStateOf(TextFieldValue("")) }
    var rentalPrice by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf(TextFieldValue("")) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Input fields
        OutlinedTextField(
            value = manufacturer,
            onValueChange = { manufacturer = it },
            label = { Text("Manufacturer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = seats,
            onValueChange = { seats = it },
            label = { Text("Number of Seats") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fuelType,
            onValueChange = { fuelType = it },
            label = { Text("Fuel Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = rentalPrice,
            onValueChange = { rentalPrice = it },
            label = { Text("Rental Price per Day") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit button
        Button(
            onClick = {
                if (manufacturer.text.isEmpty() || model.text.isEmpty() || year.text.isEmpty() ||
                    seats.text.isEmpty() || fuelType.text.isEmpty() || rentalPrice.text.isEmpty() ||
                    location.text.isEmpty()
                ) {
                    errorMessage = "All fields are required!"
                    successMessage = null
                } else {
                    errorMessage = null
                    successMessage = "Car listed successfully!"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("List Car")
        }

        Spacer(modifier = Modifier.height(8.dp))


        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        successMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
