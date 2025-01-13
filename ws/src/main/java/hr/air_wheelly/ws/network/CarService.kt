package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.models.responses.car.CarModel
import hr.air_wheelly.ws.models.responses.car.CreateNewReservationBody
import hr.air_wheelly.ws.models.responses.car.NewCarBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CarService {
    @GET("car/getallmanafacturer")
    fun getAllManufacturers(): Call<SuccessfulResponseBody<Array<AllManufacturers>>>

    @GET("car/getmodelsbyid/{id}")
    fun getModelsByManufacturerId(@Path("id") manufacturerId: String): Call<SuccessfulResponseBody<Array<CarModel>>>

    @GET("car/getfueltype")
    fun getFuelTypes(): Call<SuccessfulResponseBody<Array<String>>>

    @POST("car/createcarlisting")
    fun createCarListing(@Body newCarBody: NewCarBody): Call<SuccessfulResponseBody<Unit>>
  
    @GET("car/carlistings")
    fun getListedCars(): Call<SuccessfulResponseBody<List<CarListResponse>>>

    @GET("car/GetCarListingById/{id}")
    fun getCarListingById(@Path("id") carListingId: String): Call<SuccessfulResponseBody<CarListResponse>>

    @POST("car/CreateRental")
    fun createRental(@Body reservationBody: CreateNewReservationBody): Call<SuccessfulResponseBody<Unit>>

}