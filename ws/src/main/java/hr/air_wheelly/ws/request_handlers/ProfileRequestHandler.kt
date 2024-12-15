package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.LoggedInUserJWT
import hr.air_wheelly.ws.models.responses.LoginBody
import hr.air_wheelly.ws.models.responses.ProfileResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class ProfileRequestHandler(private val context: Context) :
    TemplateRequestHandler<ProfileResponse>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<ProfileResponse>> {
        val service = NetworkService.authService(context)

        return service.getProfile()
    }
}