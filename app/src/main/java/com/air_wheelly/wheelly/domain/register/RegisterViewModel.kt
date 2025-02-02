package com.air_wheelly.wheelly.domain.register

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.RegistrationBody
import hr.air_wheelly.ws.models.responses.RegisterResponse
import hr.air_wheelly.ws.request_handlers.RegistrationRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    fun validateInputs(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        var firstNameError = ""
        var lastNameError = ""
        var emailError = ""
        var passwordError = ""
        var confirmPasswordError = ""

        if (firstName.isEmpty()) firstNameError = "First name cannot be empty"
        if (lastName.isEmpty()) lastNameError = "Last name cannot be empty"
        if (email.isEmpty()) emailError = "Email cannot be empty"
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) emailError = "Invalid email format"
        if (password.isEmpty()) passwordError = "Password cannot be empty"
        if (confirmPassword != password) confirmPasswordError = "Passwords do not match"

        _state.value = _state.value.copy(
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )

        if (firstNameError.isEmpty() && lastNameError.isEmpty() && emailError.isEmpty() &&
            passwordError.isEmpty() && confirmPasswordError.isEmpty()) {
            performRegistration(firstName, lastName, email, password)
        }
    }

    private fun performRegistration(firstName: String, lastName: String, email: String, password: String) {
        _state.value = _state.value.copy(isLoading = true)

        val requestBody = RegistrationBody(firstName, lastName, email, password)

        viewModelScope.launch {
            try {
                val registrationRequestHandler = RegistrationRequestHandler(requestBody, context)
                registrationRequestHandler.sendRequest(object : ResponseListener<RegisterResponse> {
                    override fun onSuccessfulResponse(response: SuccessfulResponseBody<RegisterResponse>) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isRegistered = true
                        )
                        Log.d("REGISTER", "User has been registered")
                    }

                    override fun onErrorResponse(response: ErrorResponseBody) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = "Email is already in use"
                        )
                        Log.d("REGISTER", "ERROR, ${response.error_message}")
                    }

                    override fun onNetworkFailure(t: Throwable) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = "Network error occurred, please try again later"
                        )
                        Log.d("REGISTER", "ERROR, ${t.cause}")
                    }
                })
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "An error occurred: ${e.message}"
                )
                Log.d("REGISTER", "ERROR, ${e.cause}")
            }
        }
    }
}