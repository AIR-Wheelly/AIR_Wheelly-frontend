package com.air_wheelly.wheelly.domain.payment

import com.air_wheelly.wheelly.domain.BaseState
import hr.air_wheelly.core.network.CarListResponse

data class PaymentState(
    val carListingById: CarListResponse? = null,
    val isPayed: Boolean = false,
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState(isLoading, errorMessage)
