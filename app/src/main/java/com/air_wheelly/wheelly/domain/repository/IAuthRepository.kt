package com.air_wheelly.wheelly.domain.repository

import com.air_wheelly.wheelly.domain.model.User

interface IAuthRepository {
    suspend fun registerUser(firstName: String, lastName: String, email: String, password: String) : User

    suspend fun loginUser(email: String, password: String): User
}