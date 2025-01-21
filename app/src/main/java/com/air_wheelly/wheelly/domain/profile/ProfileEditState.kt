package com.air_wheelly.wheelly.domain.profile

import com.air_wheelly.wheelly.domain.BaseState
import hr.air_wheelly.ws.models.responses.UserProfileResponse

data class ProfileEditState(
    val userProfile: UserProfileResponse? = null,
    val firstName: String = "",
    val lastName: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState(isLoading, errorMessage)