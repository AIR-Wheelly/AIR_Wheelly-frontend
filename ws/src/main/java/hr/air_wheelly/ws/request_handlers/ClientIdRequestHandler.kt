package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.payment.ClientIdResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class ClientIdRequestHandler(private val context: Context) : TemplateRequestHandler<ClientIdResponse>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<ClientIdResponse>> {
        val service = NetworkService.paymentService(context)
        return service.getClientId()
    }
}