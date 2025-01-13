package com.air_wheelly.wheelly.presentation.components

import android.R
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hr.air_wheelly.core.network.CarListResponse

@Composable
fun CarCard(car: CarListResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_menu_gallery),
                contentDescription = "Car image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Model: ${car.model?.name}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Fuel Type: ${car.fuelType}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Seats: ${car.numberOfSeats}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Location: ${car.location?.adress ?: "Unknown"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Year: ${car.yearOfProduction}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Rental Price: $${car.rentalPriceType}/day",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}