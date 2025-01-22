package com.air_wheelly.wheelly.domain.statistics

import com.air_wheelly.wheelly.domain.BaseState

data class StatisticsState(

    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState(isLoading, errorMessage)