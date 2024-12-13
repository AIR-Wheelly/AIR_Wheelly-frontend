package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class CarListRequestHandler(private val context: Context) : TemplateRequestHandler<List<AllManufacturers>>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<List<AllManufacturers>>> {
        val service = NetworkService.carService(context)
        return service.getAllManufectureres()
    }
}