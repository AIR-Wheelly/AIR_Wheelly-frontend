package hr.air_wheelly.ws.models.responses

data class ProfileResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String
)
