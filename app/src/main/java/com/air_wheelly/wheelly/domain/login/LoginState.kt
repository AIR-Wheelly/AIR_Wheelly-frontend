package com.air_wheelly.wheelly.domain.login

import com.air_wheelly.wheelly.domain.BaseState

data class LoginState(
    val emailError: String? = null,
    val passwordError: String? = null,
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState(isLoading, errorMessage)