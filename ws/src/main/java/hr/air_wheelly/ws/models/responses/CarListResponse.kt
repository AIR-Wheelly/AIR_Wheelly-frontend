package hr.air_wheelly.ws.models.responses

import hr.air_wheelly.core.util.CarLocation
import hr.air_wheelly.core.util.EnumFuelType

data class CarListResponse(
    val id: Int,
    val manufacturer: String,
    val model: String,
    val year: Int,
    val numberOfSeats: Int,
    val fuelType: EnumFuelType,
    val rentalPrice: Float,
    val location: CarLocation,
    val numberOfKilometers: Int,
    val registrationNumber: String,
    val description: String,
    val renter: Int
)
