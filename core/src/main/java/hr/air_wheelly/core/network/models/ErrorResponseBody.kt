package hr.air_wheelly.core.network.models

class ErrorResponseBody(
    success: Boolean,
    message: String,
    val error_code: Int,
    val error_message: String
) : ResponseBody(success, message)