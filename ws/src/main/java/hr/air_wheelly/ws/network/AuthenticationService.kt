package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.LoggedInUserJWT
import hr.air_wheelly.ws.models.RegistrationBody
import hr.air_wheelly.ws.models.responses.GoogleLoginBody
import hr.air_wheelly.ws.models.responses.LoginBody
import hr.air_wheelly.ws.models.responses.ProfileResponse
import hr.air_wheelly.ws.models.responses.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationService {
    @POST("auth/login")
    fun loginUser(@Body loginBody: LoginBody) : Call<SuccessfulResponseBody<LoggedInUserJWT>>

    @POST("auth/tokenlogin")
    fun googleLogin(@Body loginBody: GoogleLoginBody) : Call<SuccessfulResponseBody<LoggedInUserJWT>>

    @POST("auth/register")
    fun registerUser(@Body registrationBody: RegistrationBody): Call<SuccessfulResponseBody<RegisterResponse>>

    @GET("auth/profile")
    fun getProfile() : Call<SuccessfulResponseBody<ProfileResponse>>
}