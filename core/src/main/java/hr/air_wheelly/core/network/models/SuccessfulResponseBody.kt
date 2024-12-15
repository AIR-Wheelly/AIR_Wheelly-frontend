package hr.air_wheelly.core.network.models

class SuccessfulResponseBody<T>(success: Boolean, message: String, val result: T, val token : String) : ResponseBody(success, message)

