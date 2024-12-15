package hr.air_wheelly.ws.models.responses.car

data class NewCarBody(
    val modelId: String,
    val yearOfProduction: Int,
    val fuelType: String,
    val rentalPriceType: Double,
    val numberOfSeats: Int,
    val locationId: String,
    val numberOfKilometers: Double,
    val registrationNumber: String,
    val description: String,
    val userId: String
)
