package com.air_wheelly.wheelly.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse

@Composable
fun ReservationCard(
    reservation: PastReservationsResponse,
    onClick: (PastReservationsResponse) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .clickable { onClick(reservation) },
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
                    text = "ReservationId: ${reservation.id}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Start Date: ${reservation.startDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "End Date: ${reservation.endDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Price: ${reservation.totalPrice}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}