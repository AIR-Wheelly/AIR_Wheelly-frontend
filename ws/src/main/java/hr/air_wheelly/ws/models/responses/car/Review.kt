package hr.air_wheelly.ws.models.responses.car

import hr.air_wheelly.ws.models.responses.ProfileResponse

data class Review(
    val grade: Int,
    val userId: String,
    val carListingId: String,
    val user: ProfileResponse
)
