package hr.air_wheelly.ws.models.responses

data class UserProfileResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val createdAt: String
)