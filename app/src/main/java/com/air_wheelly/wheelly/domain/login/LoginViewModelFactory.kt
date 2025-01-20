package com.air_wheelly.wheelly.domain.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.air_wheelly.ws.models.responses.ProfileResponse

class LoginViewModelFactory(
    private val context: Context,
    val callBackFunction: (ProfileResponse) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context, callBackFunction) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}