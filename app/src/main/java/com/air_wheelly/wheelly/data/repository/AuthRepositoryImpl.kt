package com.air_wheelly.wheelly.data.repository

import com.air_wheelly.wheelly.data.IAuthApiService
import com.air_wheelly.wheelly.data.dto.body.RegistrationBody
import com.air_wheelly.wheelly.domain.model.User
import com.air_wheelly.wheelly.domain.repository.IAuthRepository

class AuthRepositoryImpl(
    private val apiService: IAuthApiService
) : IAuthRepository {
    override suspend fun registerUser(firstName: String, lastName: String, email: String, password: String): User {
        val body = RegistrationBody(firstName, lastName, email, password)
        val response = apiService.registerUser(body)

        return User(firstName = response.firstName, lastName = response.lastName, email = response.email)
    }

}