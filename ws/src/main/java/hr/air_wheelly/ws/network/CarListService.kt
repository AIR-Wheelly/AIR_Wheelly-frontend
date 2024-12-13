package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarListResponse
import retrofit2.Call
import retrofit2.http.GET

interface CarListService {

    @GET("car/carlistings")
    fun getListedCars() : Call<SuccessfulResponseBody<List<CarListResponse>>>
}