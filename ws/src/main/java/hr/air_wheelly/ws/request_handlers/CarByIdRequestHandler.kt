package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class CarByIdRequestHandler(private val context: Context, private val carId: String): TemplateRequestHandler<CarListResponse>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<CarListResponse>> {
        val service = NetworkService.carService(context)
        return service.getCarListingById(carId)
    }
}