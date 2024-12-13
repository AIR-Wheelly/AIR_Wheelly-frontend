package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.LoggedInUserJWT
import hr.air_wheelly.ws.models.responses.GoogleLoginBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class TokenLoginRequestHandler(private val requestBody: GoogleLoginBody, private val context: Context) :
    TemplateRequestHandler<LoggedInUserJWT>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<LoggedInUserJWT>> {
        val service = NetworkService.authService(context)

        return service.googleLogin(requestBody)
    }
}