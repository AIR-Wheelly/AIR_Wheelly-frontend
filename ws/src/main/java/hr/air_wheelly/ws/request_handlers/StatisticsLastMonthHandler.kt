package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.statistics.LastMonthResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class StatisticsLastMonthHandler(
    private val context: Context
) : TemplateRequestHandler<LastMonthResponse>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<LastMonthResponse>> {
        val service = NetworkService.statisticsService(context)
        return service.getLastMonth()
    }
}