package hr.air_wheelly.login_google

import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import hr.air_wheelly.core.login.ILoginConfig
import hr.air_wheelly.core.login.LoginHandler
import hr.air_wheelly.core.login.LoginOutcomeListener
import hr.air_wheelly.core.login.LoginResponse
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.LoggedInUserJWT
import hr.air_wheelly.ws.models.responses.GoogleLoginBody
import hr.air_wheelly.ws.request_handlers.TokenLoginRequestHandler

class GoogleLoginHandler : LoginHandler {

    override suspend fun handleLogin(
        loginConfig: ILoginConfig,
        loginOutcomeListener: LoginOutcomeListener
    ) {

        val googleLoginConfig = loginConfig as GoogleLoginConfig
        val context = googleLoginConfig.context

        val credentialManager = CredentialManager.create(context)

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("409520546567-8f6p40c6otlal5t6anj1v34kkc5l2h7c.apps.googleusercontent.com")
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            val credential = result.credential

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)

            val googleIdToken = googleIdTokenCredential.idToken

            Log.i("ASDF", googleIdToken)

            val loginRequestHandler = TokenLoginRequestHandler(
                GoogleLoginBody(
                token = googleIdToken
            ),
                context
            )

            loginRequestHandler.sendRequest(
                object : ResponseListener<LoggedInUserJWT> {
                    override fun onSuccessfulResponse(response: SuccessfulResponseBody<LoggedInUserJWT>) {
                        val token = response.token

                        loginOutcomeListener.onSuccessfulLogin(
                            LoginResponse(
                                token = token
                            )
                        )
                    }

                    override fun onErrorResponse(response: ErrorResponseBody) {

                        Log.d("ASDF", response.error_code.toString() + response.error_message + response.message)


                        loginOutcomeListener.onFailedLogin(response.message)
                    }

                    override fun onNetworkFailure(t: Throwable) {

                        Log.d("ASDF", t.message.toString())

                        loginOutcomeListener.onFailedLogin(t.message ?: "Could not connect to network.")
                    }

                }
            )

        } catch (e: GetCredentialException) {
            Log.d("CREDENTIALERROR", e.message.toString())
        } catch (e: GoogleIdTokenParsingException) {
            Log.d("GOOGLEERROR", e.message.toString())
        } catch (e: Exception) {
            var i = e.message
        }
    }
}