package com.air_wheelly.wheelly.domain.car_list

import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.util.EnumFuelType

class CarListModel {
    fun filterCars(
        carList: List<CarListResponse>,
        fuelTypes: Set<EnumFuelType>,
        manufacturer: String,
        year: Int?
    ): List<CarListResponse> {
        return carList.filter { car ->
            (fuelTypes.isEmpty() || fuelTypes.contains(EnumFuelType.valueOf(car.fuelType!!.uppercase()))) &&
                    (manufacturer.isEmpty() || car.model?.manafacturerId?.contains(manufacturer, ignoreCase = true) == true) &&
                    (year == null || car.yearOfProduction == year)
        }
    }
}