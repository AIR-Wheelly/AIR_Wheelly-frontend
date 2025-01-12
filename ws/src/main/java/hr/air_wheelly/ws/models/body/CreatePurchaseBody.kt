package hr.air_wheelly.ws.models.body

data class CreatePurchaseBody(
    val paymentMethodNonce: String,
    val deviceData: String?,
    val amount: Float,
    val reservationId: String
)
