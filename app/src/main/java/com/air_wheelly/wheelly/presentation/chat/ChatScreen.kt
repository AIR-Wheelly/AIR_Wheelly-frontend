package com.air_wheelly.wheelly.presentation.chat

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Send
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
    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(context, reservationId, currentUserId))
    val chatMessages by viewModel.chatMessages.collectAsState()
    var message by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages) {
        if (chatMessages.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            reverseLayout = true
        ) {
            items(chatMessages.reversed()) { chatMessage ->
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = Color(0xFF4CAF50), shape = androidx.compose.foundation.shape.CircleShape)
                    .padding(8.dp)
                    .clickable {
                        if (message.text.isNotBlank()) {
                            viewModel.sendMessage(message.text)
                            message = TextFieldValue("")
                        }
                    },
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                androidx.compose.material.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}