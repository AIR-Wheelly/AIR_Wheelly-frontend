package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.UserProfileResponse
import hr.air_wheelly.ws.models.responses.UpdateProfileResponse
import hr.air_wheelly.ws.models.UpdateProfileRequest
import retrofit2.Call
import retrofit2.http.*

interface EditProfileService {
    @GET("auth/profile")
    fun getUserProfile(): Call<SuccessfulResponseBody<UserProfileResponse>>

    @PUT("auth/updateProfile")
    fun updateUserProfile(@Body updateProfileRequest: UpdateProfileRequest): Call<SuccessfulResponseBody<UpdateProfileResponse>>
}
