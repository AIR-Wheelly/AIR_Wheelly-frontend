package hr.air_wheelly.ws.models.responses

import hr.air_wheelly.core.util.CarLocation
import hr.air_wheelly.ws.models.responses.car.Review

data class CarListResponse(
    val id: String?,
    val modelId: String?,
    val yearOfProduction: Int?,
    val numberOfSeats: Int?,
    val fuelType: String?,
    val rentalPriceType: Float?,
    val locationId: String?,
    val location: CarLocation?,
    val numberOfKilometers: Int?,
    val registrationNumber: String?,
    val description: String?,
    val isActive: Boolean,
    val userId: String?,
    val user: ProfileResponse?,
    val model: CarModel?,
    val carListingPictures: List<CarListingPicture>,
    val reviews: List<Review>
)

data class CarListingPicture(
    val id: String,
    val carListingId: String,
    val image: String
)