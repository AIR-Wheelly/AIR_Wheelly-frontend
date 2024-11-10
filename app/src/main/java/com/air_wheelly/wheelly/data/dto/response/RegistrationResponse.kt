package com.air_wheelly.wheelly.data.dto.response

data class RegistrationResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String?,
    val createdAt: String
)
