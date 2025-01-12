package com.air_wheelly.wheelly.presentation.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayInputStream

@Composable
fun Base64Image(
    base64String: String?,
    modifier: Modifier
) {
    val decodedString = Base64.decode(base64String, Base64.DEFAULT)
    val inputStream = ByteArrayInputStream(decodedString)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Car Image",
            modifier = modifier
        )
    }
}