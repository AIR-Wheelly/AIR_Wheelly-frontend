package hr.air_whelly.edit_profile

data class ProfileEditConfig(
    val token: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val currentPassword: String,
    val newPassword: String?
)
