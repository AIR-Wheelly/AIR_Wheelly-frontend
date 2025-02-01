package com.air_wheelly.wheelly.domain.reservation

import com.air_wheelly.wheelly.domain.BaseState
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.models.responses.car.CarModel

data class CarState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val successMessage: String? = null,
    val manufacturers: List<AllManufacturers> = emptyList(),
    val models: List<CarModel> = emptyList(),
    val fuelTypes: List<String> = emptyList(),
    val currentLocation: LocationState? = null,
    val carDetails: CarDetailsState? = null,
    val averageGrade: Float? = null
) : BaseState(isLoading, errorMessage)

data class LocationState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = ""
)

data class CarDetailsState(
    val carId: String = "",
    val carName: String = "",
    val carModel: String = "",
    val pricePerDay: Double = 0.0
)
