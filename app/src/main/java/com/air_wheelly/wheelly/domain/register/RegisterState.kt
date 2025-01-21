package com.air_wheelly.wheelly.domain.register

import com.air_wheelly.wheelly.domain.BaseState

data class RegisterState(
    val firstNameError: String = "",
    val lastNameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
    val isRegistered: Boolean = false,
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState(isLoading, errorMessage)
