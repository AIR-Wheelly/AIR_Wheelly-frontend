package com.air_wheelly.wheelly.presentation.car_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import hr.air_wheelly.core.util.EnumFuelType

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

    val scrollState = rememberScrollState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )



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
                    /*filteredCars = sampleCars.filter {
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
                /*filteredCars.forEach { car ->
                    CarCard(car)
                }*/

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

