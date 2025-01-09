package com.air_wheelly.wheelly.presentation.car_list

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.model.CarListHandler
import com.air_wheelly.wheelly.presentation.components.CarCard
import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.util.EnumFuelType

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun CarList(
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedFuelType by remember { mutableStateOf<EnumFuelType?>(null) }

    var selectedManufacturer by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val context = LocalContext.current
    val carListHandler = remember { CarListHandler(context) }
    val carList by carListHandler.carList.collectAsState()
    val errorMessage by carListHandler.errorMessage.collectAsState()

    var filteredCarList by remember { mutableStateOf(listOf<CarListResponse>()) }

    LaunchedEffect(carList, selectedFuelType, selectedManufacturer, selectedYear) {
        filteredCarList = carList.filter { car ->
            (selectedFuelType == null || car.fuelType == selectedFuelType!!.name) &&
                    (selectedManufacturer.isEmpty() || car.model?.manafacturerId?.contains(selectedManufacturer, ignoreCase = true) == true) &&
                    (selectedYear == null || car.yearOfProduction == selectedYear)
        }
    }

    LaunchedEffect(Unit) {
        carListHandler.fetchCarList()
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
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EnumFuelType.values().forEach { fuelType ->
                        Button(
                            modifier = Modifier.fillMaxWidth(0.45f),
                            onClick = { selectedFuelType = if (selectedFuelType == fuelType) null else fuelType },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (selectedFuelType == fuelType) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        ) {
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

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        selectedFuelType = null
                        selectedManufacturer = ""
                        selectedYear = null
                    }
                ) {
                    Text("Clear Filters")
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
                if (errorMessage != null) {
                    Text("Error: $errorMessage", color = Color.Red)
                } else {
                    filteredCarList.forEach { car ->
                        CarCard(car)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        navController.navigate("createListing")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 30.dp)
                ) {
                    Text("List Your Car")
                }
            }
        }
    }
}