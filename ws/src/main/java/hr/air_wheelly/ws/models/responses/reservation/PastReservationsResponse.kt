package hr.air_wheelly.ws.models.responses.reservation

data class PastReservationsResponse(
    val id: String,
    val carListingId: String,
    val userId: String,
    val startDate: String,
    val endDate: String,
    val totalPrice: Float,
    val status: String,
    val isPaid: Boolean
)
