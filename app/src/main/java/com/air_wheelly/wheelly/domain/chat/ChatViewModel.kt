package com.air_wheelly.wheelly.domain.chat

import hr.air_wheelly.ws.network.ChatService
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.ws.models.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val context: Context,
    private val reservationId: String,
    private val currentUserId: String
) : ViewModel() {
    private val chatService = ChatService(context, reservationId)
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    init {
        viewModelScope.launch {
            chatService.startConnection()

            chatService.setOnMessageReceivedListener { newMessage ->
                if (newMessage.senderId != currentUserId) {
                    viewModelScope.launch {
                        _chatMessages.value = (_chatMessages.value + newMessage).sortedBy { it.timestamp }
                        Log.d("ChatViewModel", "New message received: $newMessage")
                    }
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            val newMessage = ChatMessage(
                senderId = currentUserId,
                reservationId = reservationId,
                message = message,
                timestamp = timestamp
            )

            chatService.sendMessage(message)
            _chatMessages.value = (_chatMessages.value + newMessage)
            Log.d("ChatViewModel", "Message sent: $newMessage")
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatService.stopConnection()
    }
}