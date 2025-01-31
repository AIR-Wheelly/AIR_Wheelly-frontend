package com.air_wheelly.wheelly.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ReviewPopup(
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedRating = remember { mutableStateOf(0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Star rating row
                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { selectedRating.value = i },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (i <= selectedRating.value) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "Rate $i stars",
                                tint = if (i <= selectedRating.value) Color(0xFFFFD700) else Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                // Action buttons row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { onConfirm(selectedRating.value) },
                        enabled = selectedRating.value > 0
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}