package hr.air_wheelly.ws.models

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val currentPassword: String,
    val newPassword: String
)