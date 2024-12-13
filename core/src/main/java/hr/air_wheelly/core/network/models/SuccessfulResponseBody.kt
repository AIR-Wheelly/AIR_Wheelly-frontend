package hr.air_wheelly.core.network.models

class SuccessfulResponseBody<T>(success: Boolean, message: String, val token: String, val response: T) : ResponseBody(success, message)