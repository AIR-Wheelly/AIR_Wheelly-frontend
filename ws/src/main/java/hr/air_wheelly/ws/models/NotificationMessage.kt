package hr.air_wheelly.ws.models

data class NotificationMessage(
    val reservationId: String,
    val title: String,
    val body: String
)
