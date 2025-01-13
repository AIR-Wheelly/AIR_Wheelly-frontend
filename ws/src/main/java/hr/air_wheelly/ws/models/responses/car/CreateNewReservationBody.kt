package hr.air_wheelly.ws.models.responses.car

data class CreateNewReservationBody(
    val carListingId: String,
    val startDate: String,
    val endDate: String
)
