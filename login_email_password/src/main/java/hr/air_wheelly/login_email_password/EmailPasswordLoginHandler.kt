package hr.air_wheelly.login_email_password

import hr.air_wheelly.core.login.LoginHandler
import hr.air_wheelly.core.login.LoginOutcomeListener
import hr.air_wheelly.core.login.LoginResponse
import hr.air_wheelly.core.login.LoginToken
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.LoggedInUserJWT
import hr.air_wheelly.ws.models.responses.LoginBody
import hr.air_wheelly.ws.request_handlers.LoginRequestHandler

class EmailPasswordLoginHandler : LoginHandler {
    override fun handleLogin(
        loginToken: LoginToken,
        loginListener: LoginOutcomeListener
    ) {
        if (loginToken !is EmailPasswordLoginToken) {
            throw IllegalArgumentException("Must receive valid instance for 'loginToken'!")
        }

        val authorizers = loginToken.getAuthorizers()
        val email = authorizers["email"]!!
        val password = authorizers["password"]!!

        val loginRequestHandler = LoginRequestHandler(LoginBody(email, password))

        loginRequestHandler.sendRequest(
            object : ResponseListener<LoggedInUserJWT> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<LoggedInUserJWT>) {
                    val loginUserData = response.data[0]

                    loginListener.onSuccessfulLogin(
                        LoginResponse(
                            jwt = loginUserData.jwt!!
                        )
                    )
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    loginListener.onFailedLogin(response.message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    loginListener.onFailedLogin(t.message ?: "Could not connect to network.")
                }

            }
        )
    }
}