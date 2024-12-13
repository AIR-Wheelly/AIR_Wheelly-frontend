package hr.air_wheelly.login_email_password

import android.content.Context
import hr.air_wheelly.core.login.ILoginConfig
import hr.air_wheelly.core.login.LoginHandler
import hr.air_wheelly.core.login.LoginOutcomeListener
import hr.air_wheelly.core.login.LoginResponse
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.LoggedInUserJWT
import hr.air_wheelly.ws.models.responses.LoginBody
import hr.air_wheelly.ws.request_handlers.LoginRequestHandler

class EmailPasswordLoginHandler(private val context: Context) : LoginHandler {
    override suspend fun handleLogin(
        loginConfig: ILoginConfig,
        loginOutcomeListener: LoginOutcomeListener
    ) {
        if (loginConfig !is EmailPasswordLoginConfig) {
            throw IllegalArgumentException("Must receive valid instance for 'loginToken'!")
        }

        val emailPasswordLoginConfig = loginConfig as EmailPasswordLoginConfig

        val email = emailPasswordLoginConfig.email
        val password = emailPasswordLoginConfig.password

        val loginRequestHandler = LoginRequestHandler(LoginBody(email, password), context)

        loginRequestHandler.sendRequest(
            object : ResponseListener<LoggedInUserJWT> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<LoggedInUserJWT>) {
                    val token = response.data.firstOrNull()?.token

                    if (token != null) {
                        loginOutcomeListener.onSuccessfulLogin(
                            LoginResponse(
                                token = token
                            )
                        )
                    } else {
                        loginOutcomeListener.onFailedLogin("Token not found in response")
                    }
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    loginOutcomeListener.onFailedLogin(response.message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    loginOutcomeListener.onFailedLogin(t.message ?: "Could not connect to network.")
                }
            }
        )
    }
}