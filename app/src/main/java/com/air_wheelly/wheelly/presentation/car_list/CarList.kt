package com.air_wheelly.wheelly.presentation.car_list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.model.CarListHandler
import com.air_wheelly.wheelly.presentation.components.CarCard
import hr.air_wheelly.core.network.CarListOutcomeListener
import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.network.ResponseCarList
import hr.air_wheelly.core.util.EnumFuelType
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CarList(
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }
    //var filteredCars by remember { mutableStateOf(sampleCars) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedFuelType by remember { mutableStateOf<EnumFuelType?>(null) }
    var selectedManufacturer by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val carList = remember { mutableStateListOf<CarListResponse>() }


    val carListHandler = CarListHandler()

    coroutineScope.launch {
        carListHandler.handleCarList(
            object : CarListOutcomeListener {
                override fun onSuccessfulCarListFetch(response: ResponseCarList) {
                    Log.d("VRVD", response.toString())

                    carList.clear()
                    carList.addAll(response.result)
                }

                override fun onFailedCarListFetch(reason: String) {
                    Log.d("NEKAJ_FRONT", "ERROR")
                }
            }
        )
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        drawerElevation = 100.dp,
        sheetElevation = 100.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Divider(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    color = Color.DarkGray,
                    thickness = 4.dp,
                )

                Text(
                    "Filter Options",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Fuel Type", style = MaterialTheme.typography.bodyLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EnumFuelType.values().forEach { fuelType ->
                        Button(onClick = { selectedFuelType = fuelType }) {
                            Text(fuelType.name)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Manufacturer", style = MaterialTheme.typography.bodyLarge)
                TextField(
                    value = selectedManufacturer,
                    onValueChange = { selectedManufacturer = it },
                    label = { Text("Manufacturer") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Year", style = MaterialTheme.typography.bodyLarge)
                TextField(
                    value = selectedYear?.toString() ?: "",
                    onValueChange = { selectedYear = it.toIntOrNull() },
                    label = { Text("Year") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    /*carList = sampleCars.filter {
                        (selectedFuelType == null || it.fuelType == selectedFuelType) &&
                                (selectedManufacturer.isBlank() || it.manufacturer.contains(selectedManufacturer, ignoreCase = true)) &&
                                (selectedYear == null || it.year == selectedYear)
                    }*/
                }) {
                    Text("Apply Filters")
                }
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetPeekHeight = 40.dp,
        sheetGesturesEnabled = true
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Log.d("ASDFFEEWFS", carList.toString())
                carList.forEach { car ->
                    CarCard(car)
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

/*val sampleCars = listOf(
    CarListResponse(
        id = 1,
        manufacturer = "Toyota",
        model = "Camry",
        numberOfSeats = 5,
        fuelType = EnumFuelType.PETROL,
        rentalPrice = 50.0f,
        location = CarLocation(latitude = 40.7128, longitude = -74.0060),
        numberOfKilometers = 5000,
        registrationNumber = "NY1234",
        description = "A comfortable sedan, great for city rides.",
        renter = 101,
        year = 2015
    ),
    CarListResponse(
        id = 2,
        manufacturer = "Honda",
        model = "Civic",
        numberOfSeats = 5,
        fuelType = EnumFuelType.HYBRID,
        rentalPrice = 45.0f,
        location = CarLocation(latitude = 37.7749, longitude = -122.4194),
        numberOfKilometers = 20000,
        registrationNumber = "SF5678",
        description = "A fuel-efficient car, perfect for long drives.",
        renter = 102,
        year = 2016
    ),
    CarListResponse(
        id = 3,
        manufacturer = "Tesla",
        model = "Model 3",
        numberOfSeats = 5,
        fuelType = EnumFuelType.ELECTRIC,
        rentalPrice = 70.0f,
        location = CarLocation(latitude = 34.0522, longitude = -118.2437),
        numberOfKilometers = 10000,
        registrationNumber = "LA91011",
        description = "A futuristic electric car with advanced features.",
        renter = 103,
        year = 2017
    )
)*/
