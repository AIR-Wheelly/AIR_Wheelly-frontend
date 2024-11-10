package com.air_wheelly.wheelly.data.dto

import com.air_wheelly.wheelly.data.dto.body.RegistrationBody
import com.air_wheelly.wheelly.data.dto.response.RegistrationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegistrationBody) : RegistrationResponse
}