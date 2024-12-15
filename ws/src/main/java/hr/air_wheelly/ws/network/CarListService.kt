package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.ResponseCarList
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CarListService {

    @GET("car/carlistings")
    fun getListedCars(
        @Header("Authorization") token: String
    ) : Call<SuccessfulResponseBody<ResponseCarList>>
}