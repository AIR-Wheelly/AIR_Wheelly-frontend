package com.air_wheelly.wheelly.domain.car_list

import com.air_wheelly.wheelly.domain.BaseState
import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.util.EnumFuelType

data class CarListState(
    val carList: List<CarListResponse> = emptyList(),
    val filteredCarList: List<CarListResponse> = emptyList(),
    val searchText: String = "",
    val selectedFuelType: Set<EnumFuelType> = EnumFuelType.values().toSet(),
    val selectedManufacturer: String = "",
    val selectedYear: Int? = null,
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState(isLoading, errorMessage)