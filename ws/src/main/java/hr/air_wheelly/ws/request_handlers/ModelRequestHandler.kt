package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.car.CarModel
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class ModelRequestHandler(private val context: Context, private val manufacturerId: String) :
    TemplateRequestHandler<Array<CarModel>>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<Array<CarModel>>> {
        val service = NetworkService.carService(context)
        return service.getModelsByManufacturerId(manufacturerId)
    }
}