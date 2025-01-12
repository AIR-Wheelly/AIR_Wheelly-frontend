package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.reservation.PastReservationsResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class PastReservationsRequestHandler(private val context: Context): TemplateRequestHandler<List<PastReservationsResponse>>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<List<PastReservationsResponse>>> {
        val service = NetworkService.pastReservationsService(context)
        return service.getAllUserReservations()
    }
}