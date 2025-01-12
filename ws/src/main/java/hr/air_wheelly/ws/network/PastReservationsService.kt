package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse
import retrofit2.Call
import retrofit2.http.GET

interface PastReservationsService {
    @GET("car/getreservationsbyuser")
    fun getAllUserReservations() : Call<SuccessfulResponseBody<List<PastReservationsResponse>>>
}