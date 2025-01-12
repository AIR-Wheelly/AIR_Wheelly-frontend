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
import com.air_wheelly.wheelly.util.LocalDateFormatter
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse

@Composable
fun ReservationCard(
    reservation: PastReservationsResponse,
    onClick: (PastReservationsResponse) -> Unit
) {

    val localDateFormatter = LocalDateFormatter()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .clickable { onClick(reservation) },
        colors = CardDefaults.cardColors(
            if (reservation.isPaid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        ),
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
                    text = "Start Date: ${localDateFormatter.toLocalDate(reservation.startDate)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "End Date: ${localDateFormatter.toLocalDate(reservation.endDate)}",
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