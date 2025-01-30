package com.air_wheelly.wheelly.domain.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChatViewModelFactory(private val context: Context, private val reservationId: String, private val currentUserId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(context, reservationId, currentUserId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}