package hr.air_wheelly.ws.models.responses.statistics

data class LastMonthResponse(
    val count: Int,
    val listing: StatisticsListing
)
