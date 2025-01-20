package com.air_wheelly.wheelly.domain.login

import android.content.Context
import android.util.Patterns
import hr.air_wheelly.core.login.ILoginConfig
import hr.air_wheelly.core.login.LoginHandler
import hr.air_wheelly.core.login.LoginOutcomeListener
import hr.air_wheelly.core.login.LoginResponse
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.login_email_password.EmailPasswordLoginConfig
import hr.air_wheelly.login_email_password.EmailPasswordLoginHandler
import hr.air_wheelly.ws.models.TokenManager
import hr.air_wheelly.ws.models.responses.ProfileResponse
import hr.air_wheelly.ws.request_handlers.ProfileRequestHandler

class LoginModel(
    private val context: Context,
    private val onLoginSuccess: (ProfileResponse) -> Unit
) {
    var state = LoginState()

    fun validateInputs(email: String, password: String): LoginState {
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

        state = state.copy(emailError = emailError, passwordError = passwordError)

        return state
    }

    suspend fun performLogin(config: ILoginConfig, onStateUpdate: (LoginState) -> Unit) {
        state = state.copy(isLoading = true, errorMessage = null)
        onStateUpdate(state)

        val loginHandler: LoginHandler = config.getHandler(context)

        loginHandler.handleLogin(config, object : LoginOutcomeListener {
            override fun onSuccessfulLogin(loginResponse: LoginResponse) {
                TokenManager.saveToken(context, loginResponse.token)
                val profileRequestHandler = ProfileRequestHandler(context)
                profileRequestHandler.sendRequest(object : ResponseListener<ProfileResponse> {
                    override fun onSuccessfulResponse(response: SuccessfulResponseBody<ProfileResponse>) {
                        state = state.copy(isLoading = false)
                        onStateUpdate(state)
                        onLoginSuccess(response.result)
                    }

                    override fun onErrorResponse(response: ErrorResponseBody) {
                        state = state.copy(isLoading = false, errorMessage = "Wrong Credentials")
                        onStateUpdate(state)
                    }

                    override fun onNetworkFailure(t: Throwable) {
                        state = state.copy(isLoading = false, errorMessage = "Network failure, please try again later")
                        onStateUpdate(state)
                    }
                })
            }

            override fun onFailedLogin(message: String) {
                state = state.copy(isLoading = false, errorMessage = "Invalid Credentials")
                onStateUpdate(state)
            }
        })
    }

    private fun ILoginConfig.getHandler(context: Context): LoginHandler {
        return when (this) {
            is EmailPasswordLoginConfig -> EmailPasswordLoginHandler(context)
            is hr.air_wheelly.login_google.GoogleLoginConfig -> hr.air_wheelly.login_google.GoogleLoginHandler()
            else -> throw IllegalArgumentException("Unknown login config")
        }
    }
}