package hr.air_wheelly.ws.models.responses

data class CarLocationResponse(
    val locationId: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val adress: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)