package hr.air_wheelly.ws.request_handlers

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarListResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class CarListRequestHandler : TemplateRequestHandler<List<CarListResponse>>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<List<CarListResponse>>> {
        val service = NetworkService.carListService

        return service.getListedCars()
    }
}