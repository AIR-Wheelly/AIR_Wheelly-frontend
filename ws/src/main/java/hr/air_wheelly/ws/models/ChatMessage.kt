package hr.air_wheelly.ws.models

data class ChatMessage(
    val reservationId: String,
    val senderId: String,
    val message: String,
    val timestamp: Long
)