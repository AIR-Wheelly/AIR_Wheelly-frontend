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
) : BaseState(isLoading, errorMessage) {
    fun filterCarList(): List<CarListResponse> {
        return carList.filter { car ->
            (selectedFuelType.isEmpty() || selectedFuelType.contains(EnumFuelType.valueOf(car.fuelType!!.uppercase()))) &&
                    (selectedManufacturer.isEmpty() || car.model?.manafacturerId?.contains(selectedManufacturer, ignoreCase = true) == true) &&
                    (selectedYear == null || car.yearOfProduction == selectedYear)
        }
    }
}