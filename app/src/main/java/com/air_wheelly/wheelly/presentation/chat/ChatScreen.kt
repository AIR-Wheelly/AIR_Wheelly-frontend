package com.air_wheelly.wheelly.presentation.chat

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.air_wheelly.wheelly.presentation.viewmodel.ChatViewModel
import com.air_wheelly.wheelly.domain.chat.ChatViewModelFactory
import hr.air_wheelly.ws.models.ChatMessage

@Composable
fun ChatScreen(reservationId: String) {
    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(context, reservationId))
    val chatMessages by viewModel.chatMessages.collectAsState()
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            chatMessages.forEach { chatMessage ->
                Text(text = "${chatMessage.senderId}: ${chatMessage.message}")
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f).padding(8.dp)
            )
            Button(onClick = {
                viewModel.sendMessage(message)
                message = ""
            }) {
                Text(text = "Send")
            }
        }
    }
}