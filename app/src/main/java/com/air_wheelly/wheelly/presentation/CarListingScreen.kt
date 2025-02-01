package com.air_wheelly.wheelly.presentation

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.air_wheelly.wheelly.domain.reservation.CarViewModel
import com.air_wheelly.wheelly.domain.reservation.CarViewModelFactory
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

    // States for dropdowns
    var selectedManufacturer by remember { mutableStateOf<AllManufacturers?>(null) }
    var selectedModel by remember { mutableStateOf<CarModel?>(null) }
    var selectedFuelType by remember { mutableStateOf<String?>(null) }
    var expandedManufacturer by remember { mutableStateOf(false) }
    var expandedModel by remember { mutableStateOf(false) }
    var expandedFuelType by remember { mutableStateOf(false) }

    // States for text fields
    var year by remember { mutableStateOf(TextFieldValue("")) }
    var seats by remember { mutableStateOf(TextFieldValue("")) }
    var rentalPrice by remember { mutableStateOf(TextFieldValue("")) }
    var numberOfKilometers by remember { mutableStateOf(TextFieldValue("")) }
    var registrationNumber by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    // Message states
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // For image selection
    val imageUris = remember { mutableStateListOf<Uri>() }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris.clear()
        imageUris.addAll(uris)
    }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List Your Car", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            com.air_wheelly.wheelly.presentation.components.BottomNavigation(
                navController = navController,
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (successMessage != null) {
                    AlertDialog(
                        onDismissRequest = { clearMessages() },
                        title = { Text("Success") },
                        text = { Text(successMessage ?: "") },
                        confirmButton = {
                            TextButton(onClick = { clearMessages() }) {
                                Text("OK")
                            }
                        }
                    )
                } else if (errorMessage != null) {
                    AlertDialog(
                        onDismissRequest = { clearMessages() },
                        title = { Text("Error") },
                        text = { Text(errorMessage ?: "") },
                        confirmButton = {
                            TextButton(onClick = { clearMessages() }) {
                                Text("OK")
                            }
                        }
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        ExposedDropdownMenuBox(
                            expanded = expandedManufacturer,
                            onExpandedChange = { expandedManufacturer = !expandedManufacturer }
                        ) {
                            OutlinedTextField(
                                value = selectedManufacturer?.name ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Manufacturer") },
                                placeholder = { Text("Select Manufacturer") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedManufacturer)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedManufacturer,
                                onDismissRequest = { expandedManufacturer = false }
                            ) {
                                state.manufacturers.forEach { manufacturer ->
                                    DropdownMenuItem(
                                        text = { Text(manufacturer.name) },
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

                        ExposedDropdownMenuBox(
                            expanded = expandedModel,
                            onExpandedChange = { expandedModel = !expandedModel }
                        ) {
                            OutlinedTextField(
                                value = selectedModel?.name ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Model") },
                                placeholder = { Text("Select Model") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedModel)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedModel,
                                onDismissRequest = { expandedModel = false }
                            ) {
                                state.models.forEach { model ->
                                    DropdownMenuItem(
                                        text = { Text(model.name) },
                                        onClick = {
                                            selectedModel = model
                                            expandedModel = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expandedFuelType,
                            onExpandedChange = { expandedFuelType = !expandedFuelType }
                        ) {
                            OutlinedTextField(
                                value = selectedFuelType ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Fuel Type") },
                                placeholder = { Text("Select Fuel Type") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFuelType)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedFuelType,
                                onDismissRequest = { expandedFuelType = false }
                            ) {
                                state.fuelTypes.forEach { fuelType ->
                                    DropdownMenuItem(
                                        text = { Text(fuelType) },
                                        onClick = {
                                            selectedFuelType = fuelType
                                            expandedFuelType = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = year,
                                onValueChange = { year = it },
                                label = { Text("Year") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = seats,
                                onValueChange = { seats = it },
                                label = { Text("Seats") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = rentalPrice,
                                onValueChange = { rentalPrice = it },
                                label = { Text("EUR/Day") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = numberOfKilometers,
                                onValueChange = { numberOfKilometers = it },
                                label = { Text("Kilometers") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = registrationNumber,
                            onValueChange = { registrationNumber = it },
                            label = { Text("License Plate") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Select Images")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(imageUris) { uri ->
                                Card(
                                    modifier = Modifier.size(100.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (selectedManufacturer == null || selectedModel == null || selectedFuelType == null ||
                                    year.text.isEmpty() || seats.text.isEmpty() || rentalPrice.text.isEmpty() ||
                                    numberOfKilometers.text.isEmpty() || registrationNumber.text.isEmpty() || description.text.isEmpty()
                                ) {
                                    errorMessage = "All fields are required!"
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

                                                carViewModel.createCarListing(newCarBody, { response ->
                                                    successMessage = "Car listed successfully!"
                                                    navController.navigate("carList")
                                                    val listingId = response.id
                                                    if (imageUris.isNotEmpty()) {
                                                        carViewModel.uploadCarImages(listingId, imageUris, {
                                                        }, {
                                                        })
                                                    }
                                                }, { error ->
                                                    errorMessage = error
                                                })
                                            }
                                        },
                                        onError = { error -> errorMessage = error }
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("List Car")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}