package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class ManufacturerRequestHandler(private val context: Context) :
    TemplateRequestHandler<Array<AllManufacturers>>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<Array<AllManufacturers>>> {
        val service = NetworkService.carService(context)

        return service.getAllManufacturers()
    }
}