package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.statistics.LastMonthResponse
import hr.air_wheelly.ws.models.responses.statistics.NumberOfRentsPerCarResponse
import retrofit2.Call
import retrofit2.http.GET

interface StatisticsService {
    @GET("statistics/NumberOfRentsPerCar")
    fun getNumberOfRentsPerCar() : Call<SuccessfulResponseBody<List<NumberOfRentsPerCarResponse>>>

    @GET("statistics/LastMonth")
    fun getLastMonth() : Call<SuccessfulResponseBody<LastMonthResponse>>
}