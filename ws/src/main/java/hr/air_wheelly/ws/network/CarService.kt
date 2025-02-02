package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarListResponse
import hr.air_wheelly.ws.models.responses.CarListingResponse
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.models.responses.car.CarModel
import hr.air_wheelly.ws.models.responses.car.CreateNewReservationBody
import hr.air_wheelly.ws.models.responses.car.NewCarBody
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CarService {
    @GET("car/getallmanafacturer")
    fun getAllManufacturers(): Call<SuccessfulResponseBody<Array<AllManufacturers>>>

    @GET("car/getmodelsbyid/{id}")
    fun getModelsByManufacturerId(@Path("id") manufacturerId: String): Call<SuccessfulResponseBody<Array<CarModel>>>

    @GET("car/getfueltype")
    fun getFuelTypes(): Call<SuccessfulResponseBody<Array<String>>>

    @POST("car/createcarlisting")
    fun createCarListing(@Body newCarBody: NewCarBody): Call<SuccessfulResponseBody<CarListingResponse>>
  
    @GET("car/carlistings")
    fun getListedCars(): Call<SuccessfulResponseBody<List<CarListResponse>>>

    @GET("car/GetCarListingById/{id}")
    fun getCarListingById(@Path("id") carListingId: String): Call<SuccessfulResponseBody<CarListResponse>>

    @POST("car/CreateRental")
    fun createRental(@Body reservationBody: CreateNewReservationBody): Call<SuccessfulResponseBody<Unit>>

    @GET("car/GetReservationsForMyCars")
    fun getReservationsForMyCars(): Call<SuccessfulResponseBody<List<PastReservationsResponse>>>

    @Multipart
    @POST("car/UploadCarListingPicture")
    fun uploadCarImages(
        @Part("listingId") listingId: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Call<SuccessfulResponseBody<Unit>>

}