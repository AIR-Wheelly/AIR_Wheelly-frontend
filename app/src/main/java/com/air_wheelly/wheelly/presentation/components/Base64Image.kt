package com.air_wheelly.wheelly.presentation.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import java.io.ByteArrayInputStream

@Composable
fun Base64Image(
    base64String: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val decoded = remember(base64String) { Base64.decode(base64String, Base64.DEFAULT) }
    val bitmap = remember(decoded) {
        val stream = ByteArrayInputStream(decoded)
        BitmapFactory.decodeStream(stream)
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = modifier.fillMaxWidth(),
            contentScale = contentScale
        )
    }
}