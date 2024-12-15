package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.RegistrationBody
import hr.air_wheelly.ws.models.responses.RegisterResponse
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CarService {
    @GET("car/getallmanafacturer")
    fun getAllManufacturers(): Call<SuccessfulResponseBody<Array<AllManufacturers>>>
}