package hr.air_wheelly.core.network

import hr.air_wheelly.core.util.CarLocation

data class CarListResponse(
    val id: String?,
    val model: CarModel?,
    val year: Int?,
    val numberOfSeats: Int?,
    val fuelType: String?,
    val rentalPrice: Float?,
    val location: CarLocation?,
    val numberOfKilometers: Int?,
    val registrationNumber: String?,
    val description: String?,
    val renter: Int?
)


data class CarModel(
    val id: String?,
    val manafacturerId: String?,
    val name: String?
)