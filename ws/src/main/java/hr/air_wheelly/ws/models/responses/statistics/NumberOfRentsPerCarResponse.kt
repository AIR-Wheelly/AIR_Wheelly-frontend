package hr.air_wheelly.ws.models.responses.statistics

import hr.air_wheelly.ws.models.responses.CarListResponse

data class NumberOfRentsPerCarResponse(
    val id: String,
    val count: Int,
    val car: CarListResponse
)
