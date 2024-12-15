package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class FuelTypeRequestHandler(private val context: Context) :
    TemplateRequestHandler<Array<String>>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<Array<String>>> {
        val service = NetworkService.carService(context)
        return service.getFuelTypes()
    }
}