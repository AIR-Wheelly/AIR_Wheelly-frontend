package hr.air_wheelly.ws.request_handlers

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.RegistrationBody
import hr.air_wheelly.ws.models.responses.RegisterResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class RegistrationRequestHandler(private val requestBody: RegistrationBody) : TemplateRequestHandler<RegisterResponse>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<RegisterResponse>> {
        val service = NetworkService.authService
        return service.registerUser(requestBody)
    }
}