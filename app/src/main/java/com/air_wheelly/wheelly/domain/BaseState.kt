package com.air_wheelly.wheelly.domain

open class BaseState(
    open val isLoading: Boolean = false,
    open val errorMessage: String? = null
)