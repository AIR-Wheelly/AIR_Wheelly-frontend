package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.car.NewCarBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class CreateCarRequestHandler(private val context: Context, private val newCarBody: NewCarBody) :
    TemplateRequestHandler<Unit>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<Unit>> {
        val service = NetworkService.carService(context)
        return service.createCarListing(newCarBody)
    }
}