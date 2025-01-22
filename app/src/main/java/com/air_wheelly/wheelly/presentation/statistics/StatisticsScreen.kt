package com.air_wheelly.wheelly.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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

@Composable
fun StatisticsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: StatisticsViewModel = viewModel(factory = StatisticsViewModelFactory(context))
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.selectedTabIndex) {
        viewModel.fetchTabData(state.selectedTabIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        TabRow(
            selectedTabIndex = state.selectedTabIndex
        ) {
            viewModel.tabOptions.forEachIndexed { index, title ->
                Tab(
                    selected = state.selectedTabIndex == index,
                    onClick = { viewModel.selectTab(index) },
                    text = { Text(title) }
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.errorMessage != null) {
                com.air_wheelly.wheelly.presentation.components.AlertDialog(
                    title = "Error",
                    message = state.errorMessage.toString(),
                    onDismiss = { viewModel.clearMessages() }
                )
            } else {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        when (state.selectedTabIndex) {
                            0 -> PerCarStatisticsScreen(state.numberOfRentsPerCar)
                            1 -> LastMonthStatisticsScreen(state.lastMonth)
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