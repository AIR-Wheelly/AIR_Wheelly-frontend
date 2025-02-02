package com.air_wheelly.wheelly.domain.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.login.ILoginConfig
import hr.air_wheelly.login_email_password.EmailPasswordLoginConfig
import hr.air_wheelly.ws.models.responses.ProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val context: Context,
    val onLoginSuccess: (ProfileResponse) -> Unit
) : ViewModel(){
    private val model = LoginModel(context, onLoginSuccess)
    private val _state = MutableStateFlow(model.state)
    val state: StateFlow<LoginState> = _state

    fun validateInputs(email: String, password: String) {
        val newState = model.validateInputs(email, password)
        _state.value = newState

        if (newState.emailError == null && newState.passwordError == null) {
            val loginConfig = EmailPasswordLoginConfig(email = email, password = password)
            performLogin(loginConfig)
        }
    }

    fun performLogin(config: ILoginConfig) {
        viewModelScope.launch {
            model.performLogin(config) { newState ->
                _state.value = newState
            }
        }
    }
}