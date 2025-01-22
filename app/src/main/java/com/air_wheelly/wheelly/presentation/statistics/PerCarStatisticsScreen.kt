package com.air_wheelly.wheelly.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.air_wheelly.wheelly.presentation.components.CarRentStatisticsCard
import hr.air_wheelly.ws.models.responses.statistics.NumberOfRentsPerCarResponse

@Composable
fun PerCarStatisticsScreen(
    numberOfRentsPerCar: List<NumberOfRentsPerCarResponse>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        numberOfRentsPerCar.forEach { rentsPerCar ->
            CarRentStatisticsCard(rentsPerCar)
        }
    }
}