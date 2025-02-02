package com.air_wheelly.wheelly.presentation.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.air_wheelly.ws.models.responses.statistics.LastMonthResponse

@Composable
fun LastMonthStatisticsScreen(lastMonth: LastMonthResponse?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(0.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Manufacturer: ${lastMonth?.listing?.car?.model?.manufacturerName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Model: ${lastMonth?.listing?.car?.model?.name}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Registration: ${lastMonth?.listing?.car?.registrationNumber}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Times Rented: ${lastMonth?.listing?.count}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}