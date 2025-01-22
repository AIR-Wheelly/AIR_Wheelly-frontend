package com.air_wheelly.wheelly.presentation.statistics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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


}