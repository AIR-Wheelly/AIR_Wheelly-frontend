package hr.air_wheelly.ws.models.responses

import java.util.*

data class CarListResponse(
    val id: String,
    val modelId: String,
    val yearOfProduction: Int,
    val numberOfSeats: Int,
    val fuelType: String,
    val rentalPrice: Float,
    val locationId: String,
    val location: String?,
    val numberOfKilometers: Int,
    val registrationNumber: String,
    val description: String,
    val isActive: Boolean,
    val userId: String,
    val model: CarModel,
    val carListingPictures: Base64?
)
