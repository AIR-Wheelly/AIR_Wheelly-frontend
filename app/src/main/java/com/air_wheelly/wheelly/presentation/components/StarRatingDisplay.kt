package com.air_wheelly.wheelly.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun StarRating(
    rating: Float?,
    modifier: Modifier = Modifier,
    starSize: Dp = 24.dp,
    filledColor: Color = Color(0xFFFFD700),
    unfilledColor: Color = Color.LightGray
) {
    val totalStars = 5
    val filledStars = floor(rating!!)
    val partialStar = rating - filledStars
    val unfilledStars = totalStars - ceil(rating)

    Row(modifier = modifier) {
        repeat(filledStars.toInt()) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = filledColor,
                modifier = Modifier.size(starSize)
            )
        }

        if (partialStar > 0) {
            Box(
                modifier = Modifier
                    .size(starSize)
                    .clipToBounds()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = unfilledColor,
                    modifier = Modifier.size(starSize)
                )
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = filledColor,
                    modifier = Modifier
                        .size(starSize)
                        .fillMaxWidth(partialStar)
                )
            }
        }

        repeat(unfilledStars.toInt()) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = unfilledColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}