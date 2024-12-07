package hr.air_wheelly.ws.models.responses

data class RegisterResponse(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val createdAt: String? = null
)