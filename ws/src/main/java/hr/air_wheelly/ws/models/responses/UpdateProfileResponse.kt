package hr.air_wheelly.ws.models.responses

data class UpdateProfileResponse(
    val message: String,
    val user: UserProfileResponse
)
