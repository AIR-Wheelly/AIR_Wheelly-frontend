package hr.air_wheelly.ws.network
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.LoggedInUserJWT
import hr.air_wheelly.ws.models.RegistrationBody
import hr.air_wheelly.ws.models.UpdateProfileRequest
import hr.air_wheelly.ws.models.responses.*
import retrofit2.Call
import retrofit2.http.*

interface EditProfileService {
    @GET("auth/profile")
    fun getUserProfile(
        @Header("Authorization") token: String // Authorization token in header
    ): Call<SuccessfulResponseBody<UserProfileResponse>>

    @PUT("auth/updateProfile")
    fun updateUserProfile(
        @Header("Authorization") token: String, // Authorization token in header
        @Body updateProfileRequest: UpdateProfileRequest
    ): Call<SuccessfulResponseBody<UpdateProfileResponse>>
}