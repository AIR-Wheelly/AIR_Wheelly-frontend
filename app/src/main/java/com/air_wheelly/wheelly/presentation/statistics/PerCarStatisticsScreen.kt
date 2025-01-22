package com.air_wheelly.wheelly.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.statistics.StatisticsViewModel
import com.air_wheelly.wheelly.domain.statistics.StatisticsViewModelFactory
import com.air_wheelly.wheelly.presentation.components.CarRentStatisticsCard

@Composable
fun PerCarStatisticsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: StatisticsViewModel = viewModel(factory = StatisticsViewModelFactory(context))
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNumberOfListingsPerCar()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.numberOfRentsPerCar.forEach { rentsPerCar ->
                        CarRentStatisticsCard(rentsPerCar)
                    }
                }
            }
        }
    }
}