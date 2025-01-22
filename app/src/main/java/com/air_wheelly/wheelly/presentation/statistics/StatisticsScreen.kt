package com.air_wheelly.wheelly.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StatisticsScreen(
    navController: NavController
) {
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
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { navController.navigate("statPerCar") }
                    ) {
                        Text(text = "Number of Rents Per Car")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { navController.navigate("lastMonth") }
                    ) {
                        Text(text = "Last Month")
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