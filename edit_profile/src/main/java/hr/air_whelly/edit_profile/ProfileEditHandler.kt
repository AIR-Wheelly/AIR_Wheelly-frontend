package hr.air_whelly.edit_profile
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.core.profile.ProfileEditOutcomeListener
import hr.air_wheelly.ws.models.responses.UpdateProfileResponse
import hr.air_wheelly.ws.models.UpdateProfileRequest
import hr.air_wheelly.ws.request_handlers.ProfileEditRequestHandler

class ProfileEditHandler {

    suspend fun handleProfileEdit(
        updateProfileRequest: UpdateProfileRequest,
        profileEditOutcomeListener: ProfileEditOutcomeListener
    ) {
        val profileEditRequestHandler = ProfileEditRequestHandler(updateProfileRequest)

        profileEditRequestHandler.sendRequest(
            object : ResponseListener<UpdateProfileResponse> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<UpdateProfileResponse>) {
                    profileEditOutcomeListener.onSuccessfulProfileEdit(response.message)
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    profileEditOutcomeListener.onFailedProfileEdit(response.message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    profileEditOutcomeListener.onFailedProfileEdit(t.message ?: "Could not connect to network.")
                }
            }
        )
    }
}



