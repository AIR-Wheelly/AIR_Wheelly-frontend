package com.air_wheelly.wheelly.presentation.car_listing

import CarViewModel
import CarViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.models.responses.car.CarModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListingScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val carViewModel: CarViewModel = viewModel(factory = CarViewModelFactory(context))

    val manufacturers by carViewModel.manufacturers.collectAsState()
    val models by carViewModel.models.collectAsState()

    var selectedManufacturer by remember { mutableStateOf<AllManufacturers?>(null) }
    var selectedModel by remember { mutableStateOf<CarModel?>(null) }
    var expandedManufacturer by remember { mutableStateOf(false) }
    var expandedModel by remember { mutableStateOf(false) }

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
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "List Your Car for Rent",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            OutlinedTextField(
                value = selectedManufacturer?.name ?: "Select Manufacturer",
                onValueChange = {},
                label = { Text("Manufacturer") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedManufacturer = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = expandedManufacturer,
                onDismissRequest = { expandedManufacturer = false }
            ) {
                manufacturers.forEach { manufacturer ->
                    DropdownMenuItem(
                        text = { Text(text = manufacturer.name) },
                        onClick = {
                            selectedManufacturer = manufacturer
                            expandedManufacturer = false
                            carViewModel.fetchModels(manufacturer.id)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            OutlinedTextField(
                value = selectedModel?.name ?: "Select Model",
                onValueChange = {},
                label = { Text("Model") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedModel = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )
            DropdownMenu(
                expanded = expandedModel,
                onDismissRequest = { expandedModel = false }
            ) {
                models.forEach { model ->
                    DropdownMenuItem(
                        text = { Text(text = model.name) },
                        onClick = {
                            selectedModel = model
                            expandedModel = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = seats,
            onValueChange = { seats = it },
            label = { Text("Number of Seats") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fuelType,
            onValueChange = { fuelType = it },
            label = { Text("Fuel Type") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = rentalPrice,
            onValueChange = { rentalPrice = it },
            label = { Text("Rental Price per Day") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedManufacturer == null || selectedModel == null || model.text.isEmpty() || year.text.isEmpty() ||
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