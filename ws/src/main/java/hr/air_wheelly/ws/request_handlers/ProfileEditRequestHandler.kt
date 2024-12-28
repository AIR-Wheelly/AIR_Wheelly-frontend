package hr.air_wheelly.ws.request_handlers

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.UpdateProfileResponse
import hr.air_wheelly.ws.models.UpdateProfileRequest
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class ProfileEditRequestHandler(private val updateProfileRequest: UpdateProfileRequest) :
    TemplateRequestHandler<UpdateProfileResponse>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<UpdateProfileResponse>> {
        val service = NetworkService.profileService

        return service.updateUserProfile("token",updateProfileRequest)
    }
}
