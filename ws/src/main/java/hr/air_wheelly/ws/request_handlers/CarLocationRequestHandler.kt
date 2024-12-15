package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarLocationResponse
import hr.air_wheelly.ws.models.responses.car.CarLocationBody
import hr.air_wheelly.ws.models.responses.car.NewCarBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class CarLocationRequestHandler(private val context: Context, private val carLocationBody: CarLocationBody) :
    TemplateRequestHandler<CarLocationResponse>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<CarLocationResponse>> {
        val service = NetworkService.locationService(context)
        return service.sendLocation(carLocationBody)
    }
}