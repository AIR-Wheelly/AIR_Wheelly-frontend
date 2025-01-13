package com.air_wheelly.wheelly.presentation.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayInputStream

@Composable
fun Base64Image(
    base64String: String?,
    modifier: Modifier = Modifier
) {
    val decodedString = remember(base64String) { Base64.decode(base64String, Base64.DEFAULT) }
    val bitmap = remember(decodedString) {
        val inputStream = ByteArrayInputStream(decodedString)
        BitmapFactory.decodeStream(inputStream)
    }

    if (bitmap != null) {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Car Image",
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        )
    }
}