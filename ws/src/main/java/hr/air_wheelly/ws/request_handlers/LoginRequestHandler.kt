package hr.air_wheelly.ws.request_handlers

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.LoggedInUserJWT
import hr.air_wheelly.ws.models.responses.LoginBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class LoginRequestHandler(private val requestBody: LoginBody) :
    TemplateRequestHandler<LoggedInUserJWT>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<LoggedInUserJWT>> {
        val service = NetworkService.authService

        return service.loginUser(requestBody)
    }
}