package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.car.CreateNewReservationBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class CarReservationRequestHandler(private val context: Context, private val createNewReservationBody: CreateNewReservationBody) :
    TemplateRequestHandler<Unit>() {

    public override fun getServiceCall(): Call<SuccessfulResponseBody<Unit>> {
        val service = NetworkService.carService(context)
        return service.createRental(createNewReservationBody)
    }
}