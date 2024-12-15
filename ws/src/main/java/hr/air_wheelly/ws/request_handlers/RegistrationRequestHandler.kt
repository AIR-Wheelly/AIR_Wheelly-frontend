package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.RegistrationBody
import hr.air_wheelly.ws.models.responses.RegisterResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class RegistrationRequestHandler(private val requestBody: RegistrationBody, private val context: Context) :
    TemplateRequestHandler<RegisterResponse>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<RegisterResponse>> {
        val service = NetworkService.authService(context)

        return service.registerUser(requestBody)
    }
}