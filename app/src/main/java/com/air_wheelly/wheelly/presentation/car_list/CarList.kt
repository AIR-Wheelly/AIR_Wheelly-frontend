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
    var selectedFuelType by remember { mutableStateOf(EnumFuelType.values().toSet()) }
    var selectedManufacturer by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf<Int?>(null) }

    var tempSelectedFuelType by remember { mutableStateOf(EnumFuelType.values().toSet()) }
    var tempSelectedManufacturer by remember { mutableStateOf("") }
    var tempSelectedYear by remember { mutableStateOf<Int?>(null) }

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
            (selectedFuelType == null || selectedFuelType.contains(EnumFuelType.valueOf(car.fuelType!!.toUpperCase()))) &&
                    (selectedManufacturer.isEmpty() || car.model?.manafacturerId?.contains(selectedManufacturer, ignoreCase = true) == true) &&
                    (selectedYear == null || car.yearOfProduction == selectedYear)
        }
    }

    LaunchedEffect(Unit) {
        carListHandler.fetchCarList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                drawerElevation = 100.dp,
                sheetElevation = 100.dp,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                sheetBackgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
                sheetPeekHeight = 55.dp,
                sheetGesturesEnabled = true,
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
                                    onClick = {
                                        tempSelectedFuelType = if (tempSelectedFuelType.contains(fuelType)) tempSelectedFuelType - fuelType else tempSelectedFuelType + fuelType
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = if (tempSelectedFuelType.contains(fuelType)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    Text(fuelType.name)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Manufacturer", style = MaterialTheme.typography.bodyLarge)
                        TextField(
                            value = tempSelectedManufacturer,
                            onValueChange = { tempSelectedManufacturer = it },
                            label = { Text("Manufacturer") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Year", style = MaterialTheme.typography.bodyLarge)
                        TextField(
                            value = tempSelectedYear?.toString() ?: "",
                            onValueChange = { tempSelectedYear = it.toIntOrNull() },
                            label = { Text("Year") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedFuelType = tempSelectedFuelType
                                selectedManufacturer = tempSelectedManufacturer
                                selectedYear = tempSelectedYear
                            }
                        ) {
                            Text("Apply Filters")
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedFuelType = EnumFuelType.values().toSet()
                                selectedManufacturer = ""
                                selectedYear = null
                                tempSelectedFuelType = EnumFuelType.values().toSet()
                                tempSelectedManufacturer = ""
                                tempSelectedYear = null
                            }
                        ) {
                            Text("Clear Filters")
                        }
                    }
                }
            ) {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 0.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (errorMessage != null) {
                            Text("Error: $errorMessage", color = Color.Red)
                        } else {
                            filteredCarList.forEach { car ->
                                CarCard(car)
                            }
                        }
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