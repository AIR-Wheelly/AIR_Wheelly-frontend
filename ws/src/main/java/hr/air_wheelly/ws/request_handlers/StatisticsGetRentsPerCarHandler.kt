package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.statistics.NumberOfRentsPerCarResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class StatisticsGetRentsPerCarHandler(
    private val context: Context
) : TemplateRequestHandler<List<NumberOfRentsPerCarResponse>>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<List<NumberOfRentsPerCarResponse>>> {
        val service = NetworkService.statisticsService(context)
        return service.getNumberOfRentsPerCar()
    }
}