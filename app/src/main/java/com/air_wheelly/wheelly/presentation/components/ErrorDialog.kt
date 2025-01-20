package com.air_wheelly.wheelly.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Error", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Text(text = errorMessage, style = MaterialTheme.typography.bodyMedium)
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text(text = "OK")
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}