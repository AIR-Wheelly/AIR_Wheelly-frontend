package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarLocationResponse
import hr.air_wheelly.ws.models.responses.car.CarLocationBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call


class CarListRequestHandler(private val context: Context) :
    TemplateRequestHandler<List<CarListResponse>>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<List<CarListResponse>>> {
        val service = NetworkService.carService(context)
        return service.getListedCars()
    }
}