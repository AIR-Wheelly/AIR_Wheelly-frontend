package com.air_wheelly.wheelly.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
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
fun StatisticsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: StatisticsViewModel = viewModel(factory = StatisticsViewModelFactory(context))
    val state by viewModel.state.collectAsState()

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
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    state.numberOfRentsPerCar.forEach { numberOfRents ->
                        CarRentStatisticsCard(numberOfRents)
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