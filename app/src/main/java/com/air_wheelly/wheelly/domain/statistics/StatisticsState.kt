package com.air_wheelly.wheelly.domain.statistics

import com.air_wheelly.wheelly.domain.BaseState
import hr.air_wheelly.ws.models.responses.statistics.LastMonthResponse
import hr.air_wheelly.ws.models.responses.statistics.NumberOfRentsPerCarResponse

data class StatisticsState(
    val selectedTabIndex: Int = 0,
    val lastMonth: LastMonthResponse? = null,
    val numberOfRentsPerCar: List<NumberOfRentsPerCarResponse> = emptyList(),
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState(isLoading, errorMessage)