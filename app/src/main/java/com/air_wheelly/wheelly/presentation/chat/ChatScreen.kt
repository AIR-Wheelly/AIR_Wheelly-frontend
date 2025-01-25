package com.air_wheelly.wheelly.presentation.chat

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.air_wheelly.wheelly.domain.chat.ChatViewModel
import com.air_wheelly.wheelly.domain.chat.ChatViewModelFactory
import com.air_wheelly.wheelly.presentation.components.ChatBubble

@Composable
fun ChatScreen(reservationId: String, currentUserId: String) {
    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(context, reservationId))
    val chatMessages by viewModel.chatMessages.collectAsState()
    var message by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            reverseLayout = true
        ) {
            items(chatMessages) { chatMessage ->
                ChatBubble(
                    chatMessage = chatMessage,
                    isCurrentUser = chatMessage.senderId == currentUserId
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (message.text.isNotBlank()) {
                            viewModel.sendMessage(message.text)
                            message = TextFieldValue("")
                        }
                    }
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                color = Color(0xFFEFEFEF),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        innerTextField()
                    }
                }
            )
            Button(
                onClick = {
                    if (message.text.isNotBlank()) {
                        viewModel.sendMessage(message.text)
                        message = TextFieldValue("")
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "Send")
            }
        }
    }
}