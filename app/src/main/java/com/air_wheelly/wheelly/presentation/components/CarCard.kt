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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hr.air_wheelly.ws.models.responses.CarListResponse

@Composable
fun CarCard(car: CarListResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .aspectRatio(10f / 13f),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(14f / 10f)
            ) {
                if (!car.carListingPictures.isNullOrEmpty()) {
                    car.carListingPictures.first().image?.let { imageBase64 ->
                        Base64Image(
                            base64String = imageBase64,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Car image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${car.model?.manufacturerName} ${car.model?.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${car.rentalPriceType} EUR/day",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${car.yearOfProduction} | ${car.fuelType}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${car.location?.adress ?: "Unknown"}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}