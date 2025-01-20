package com.air_wheelly.wheelly.domain.login

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.login.ILoginConfig
import hr.air_wheelly.core.login.LoginHandler
import hr.air_wheelly.core.login.LoginOutcomeListener
import hr.air_wheelly.core.login.LoginResponse
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.login_email_password.EmailPasswordLoginConfig
import hr.air_wheelly.login_email_password.EmailPasswordLoginHandler
import hr.air_wheelly.login_google.GoogleLoginConfig
import hr.air_wheelly.login_google.GoogleLoginHandler
import hr.air_wheelly.ws.models.TokenManager
import hr.air_wheelly.ws.models.responses.ProfileResponse
import hr.air_wheelly.ws.request_handlers.ProfileRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val context: Context,
    val onLoginSuccess: (ProfileResponse) -> Unit
) : ViewModel(){
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun performLogin(config: ILoginConfig) {
        _state.value = LoginState(isLoading = false, errorMessage = null)

        val loginHandler: LoginHandler = when (config) {
            is EmailPasswordLoginConfig -> EmailPasswordLoginHandler(context)
            is GoogleLoginConfig -> GoogleLoginHandler()
            else -> throw Exception("Unknown login config")
        }

        viewModelScope.launch {
            loginHandler.handleLogin(config,
                object : LoginOutcomeListener {
                    override fun onSuccessfulLogin(loginResponse: LoginResponse) {
                        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                        TokenManager.saveToken(context, loginResponse.token)
                        Toast.makeText(context, "Login successful! ${loginResponse.token}", Toast.LENGTH_SHORT).show()

                        val profileRequestHandler = ProfileRequestHandler(context)
                        profileRequestHandler.sendRequest(object : ResponseListener<ProfileResponse> {
                            override fun onSuccessfulResponse(response: SuccessfulResponseBody<ProfileResponse>) {
                                _state.value = _state.value.copy(isLoading = false)
                                onLoginSuccess(response.result)
                            }

                            override fun onErrorResponse(response: ErrorResponseBody) {
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    errorMessage = "Wrong Credentials"
                                )
                                Log.d("LOGIN", "Error: ${response.error_message}")
                            }

                            override fun onNetworkFailure(t: Throwable) {
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    errorMessage = "Wrong Credentials"
                                )
                                Log.d("LOGIN", "Error: ${t.cause}")
                            }
                        })
                    }

                    override fun onFailedLogin(message: String) {
                        _state.value = LoginState(
                            isLoading = false,
                            errorMessage = "Invalid Credentials"
                        )
                        Log.d("LOGIN", "Error: ${message}")
                    }
                })
        }
    }

    fun validateInputs(email: String, password: String) {
        var emailError: String? = null
        var passwordError: String? = null

        if (email.isEmpty()) {
            emailError = "Email cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
        }

        if (password.isEmpty()) {
            passwordError = "Password cannot be empty"
        }

        _state.value = LoginState(
            emailError = emailError,
            passwordError = passwordError
        )

        if (emailError == null && passwordError == null) {
            val loginConfig = EmailPasswordLoginConfig(email = email, password = password)
            performLogin(loginConfig)
        }
    }
}