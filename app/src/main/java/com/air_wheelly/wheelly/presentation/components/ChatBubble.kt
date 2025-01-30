package com.air_wheelly.wheelly.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hr.air_wheelly.ws.models.ChatMessage

@Composable
fun ChatBubble(chatMessage: ChatMessage, isCurrentUser: Boolean) {
    val timestamp = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        .format(java.util.Date(chatMessage.timestamp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        Text(
            text = chatMessage.message,
            modifier = Modifier
                .background(
                    color = if (isCurrentUser) Color(0xFFD1F7C4) else Color(0xFFF1F0F0),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        )
        Text(
            text = timestamp,
            color = Color.Gray,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}