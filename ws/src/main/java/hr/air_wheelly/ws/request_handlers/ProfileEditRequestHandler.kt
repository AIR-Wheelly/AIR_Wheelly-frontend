package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.UpdateProfileResponse
import hr.air_wheelly.ws.models.UpdateProfileRequest
import hr.air_wheelly.ws.models.responses.UserProfileResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call
class ProfileEditRequestHandler(private val context: Context) :
    TemplateRequestHandler<UserProfileResponse>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<UserProfileResponse>> {
        val service = NetworkService.profileService(context)

        return service.getUserProfile()
    }
}
