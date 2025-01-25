package com.air_wheelly.wheelly.domain.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.ws.models.ChatMessage
import hr.air_wheelly.ws.network.SignalRService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val context: Context, private val reservationId: String, private val currentUserId: String) : ViewModel() {
    private val signalRService = SignalRService(context, reservationId)
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    init {
        viewModelScope.launch {
            signalRService.startConnection()
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            signalRService.sendMessage(message)
            // Add the sent message to the chatMessages list
            val newMessage = ChatMessage(senderId = currentUserId, reservationId = reservationId,message = message)
            _chatMessages.value = _chatMessages.value + newMessage
        }
    }

    override fun onCleared() {
        super.onCleared()
        signalRService.stopConnection()
    }
}