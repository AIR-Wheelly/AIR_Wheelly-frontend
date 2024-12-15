package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarLocationResponse
import hr.air_wheelly.ws.models.responses.car.CarLocationBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationService {
    @POST("location/createlocation")
    fun sendLocation(@Body locationBody: CarLocationBody): Call<SuccessfulResponseBody<CarLocationResponse>>
}