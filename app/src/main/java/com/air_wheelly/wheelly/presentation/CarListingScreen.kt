package com.air_wheelly.wheelly.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.air_wheelly.wheelly.domain.reservation.CarViewModel
import com.air_wheelly.wheelly.domain.reservation.CarViewModelFactory
import com.air_wheelly.wheelly.presentation.components.ErrorDialog
import hr.air_wheelly.ws.models.responses.ProfileResponse
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.models.responses.car.CarModel
import hr.air_wheelly.ws.models.responses.car.NewCarBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListingScreen(
    navController: NavController,
    user: ProfileResponse
) {
    val context = LocalContext.current
    val carViewModel: CarViewModel = viewModel(factory = CarViewModelFactory(context))
    val state by carViewModel.state.collectAsState()

    var selectedManufacturer by remember { mutableStateOf<AllManufacturers?>(null) }
    var selectedModel by remember { mutableStateOf<CarModel?>(null) }
    var selectedFuelType by remember { mutableStateOf<String?>(null) }
    var expandedManufacturer by remember { mutableStateOf(false) }
    var expandedModel by remember { mutableStateOf(false) }
    var expandedFuelType by remember { mutableStateOf(false) }

    var year by remember { mutableStateOf(TextFieldValue("")) }
    var seats by remember { mutableStateOf(TextFieldValue("")) }
    var rentalPrice by remember { mutableStateOf(TextFieldValue("")) }
    var numberOfKilometers by remember { mutableStateOf(TextFieldValue("")) }
    var registrationNumber by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            errorMessage = "Location permission is required to list your car"
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                if (successMessage != null) {
                    ErrorDialog(
                        errorMessage = successMessage.toString(),
                        { clearMessages() }
                    )
                } else if (errorMessage != null) {
                    ErrorDialog(
                        errorMessage = successMessage.toString(),
                        { clearMessages() }
                    )
                } else {
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
                            state.manufacturers.forEach { manufacturer ->
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
                            state.models.forEach { model ->
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

                    Box {
                        OutlinedTextField(
                            value = selectedFuelType ?: "Select Fuel Type",
                            onValueChange = {},
                            label = { Text("Fuel Type") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { expandedFuelType = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = expandedFuelType,
                            onDismissRequest = { expandedFuelType = false }
                        ) {
                            state.fuelTypes.forEach { fuelType ->
                                DropdownMenuItem(
                                    text = { Text(text = fuelType) },
                                    onClick = {
                                        selectedFuelType = fuelType
                                        expandedFuelType = false
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
                        value = rentalPrice,
                        onValueChange = { rentalPrice = it },
                        label = { Text("Rental Price per Day") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = numberOfKilometers,
                        onValueChange = { numberOfKilometers = it },
                        label = { Text("Number of Kilometers") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = registrationNumber,
                        onValueChange = { registrationNumber = it },
                        label = { Text("Registration Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (selectedManufacturer == null || selectedModel == null || selectedFuelType == null || year.text.isEmpty() ||
                                seats.text.isEmpty() || rentalPrice.text.isEmpty() ||
                                numberOfKilometers.text.isEmpty() || registrationNumber.text.isEmpty() || description.text.isEmpty()
                            ) {
                                errorMessage = "All fields are required!"
                                successMessage = null
                            } else {
                                carViewModel.getCurrentLocation(
                                    onLocationReceived = { location ->
                                        carViewModel.sendLocationToApi(location) { locationId ->
                                            val newCarBody = NewCarBody(
                                                modelId = selectedModel!!.id,
                                                yearOfProduction = year.text.toInt(),
                                                fuelType = selectedFuelType!!,
                                                rentalPriceType = rentalPrice.text.toDouble(),
                                                numberOfSeats = seats.text.toInt(),
                                                locationId = locationId,
                                                numberOfKilometers = numberOfKilometers.text.toDouble(),
                                                registrationNumber = registrationNumber.text,
                                                description = description.text,
                                                userId = user.id
                                            )
                                            carViewModel.createCarListing(newCarBody, {
                                                successMessage = "Car listed successfully!"
                                                errorMessage = null
                                            }, { error ->
                                                errorMessage = error
                                                successMessage = null
                                            })
                                        }
                                    },
                                    onError = { error ->
                                        errorMessage = error
                                    }
                                )
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
        }
        com.air_wheelly.wheelly.presentation.components.BottomNavigation(
            navController = navController,
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxWidth()
        )
    }
}