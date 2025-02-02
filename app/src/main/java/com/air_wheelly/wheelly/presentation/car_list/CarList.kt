
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.car_list.CarListViewModel
import com.air_wheelly.wheelly.domain.car_list.CarListViewModelFactory
import com.air_wheelly.wheelly.presentation.components.AlertDialog
import com.air_wheelly.wheelly.presentation.components.BottomNavigation
import com.air_wheelly.wheelly.presentation.components.CarCard
import hr.air_wheelly.core.util.EnumFuelType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun CarList(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: CarListViewModel =
        viewModel(factory = CarListViewModelFactory(context))
    val state by viewModel.state.collectAsState()

    var tempSelectedFuelType by remember { mutableStateOf(EnumFuelType.values().toSet()) }
    var tempSelectedManufacturer by remember { mutableStateOf("") }
    var tempSelectedYear by remember { mutableStateOf<Int?>(null) }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    LaunchedEffect(Unit) {
        viewModel.fetchCarList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.errorMessage != null -> {
                    AlertDialog(
                        title = "Error",
                        message = state.errorMessage.toString(),
                        onDismiss = { viewModel.clearError() }
                    )
                }
                else -> {
                    BottomSheetScaffold(
                        scaffoldState = scaffoldState,
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
                                        .align(Alignment.CenterHorizontally)
                                        .fillMaxWidth(0.9f),
                                    thickness = 3.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                )

                                Text(
                                    "Filter Options",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
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
                                                tempSelectedFuelType =
                                                    if (tempSelectedFuelType.contains(fuelType))
                                                        tempSelectedFuelType - fuelType
                                                    else
                                                        tempSelectedFuelType + fuelType
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (tempSelectedFuelType.contains(fuelType))
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.secondary
                                            )
                                        ) {
                                            Text(fuelType.name)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = tempSelectedManufacturer,
                                    onValueChange = { tempSelectedManufacturer = it },
                                    label = { Text("Manufacturer") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = tempSelectedYear?.toString() ?: "",
                                    onValueChange = { tempSelectedYear = it.toIntOrNull() },
                                    label = { Text("Year") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        viewModel.applyFilters(
                                            tempSelectedFuelType,
                                            tempSelectedManufacturer,
                                            tempSelectedYear
                                        )
                                    }
                                ) {
                                    Text("Apply Filters")
                                }

                                FilledTonalButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        viewModel.clearFilters()
                                        tempSelectedFuelType = EnumFuelType.values().toSet()
                                        tempSelectedManufacturer = ""
                                        tempSelectedYear = null
                                    }
                                ) {
                                    Text("Clear Filters")
                                }
                            }
                        }
                    ) { innerPadding ->
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = innerPadding,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.filteredCarList) { car ->
                                CarCard(car = car) {
                                    navController.navigate("car_reservation/${car.id}")
                                }
                            }
                        }
                    }
                }
            }
        }

        BottomNavigation(
            navController = navController,
            modifier = Modifier.fillMaxWidth()
        )
    }
}