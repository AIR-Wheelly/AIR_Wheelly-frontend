package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.body.CreatePurchaseBody
import hr.air_wheelly.ws.models.responses.payment.CreatePurchaseResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class CreateTransactionRequestHandler(private val context: Context, private val createPurchaseBody: CreatePurchaseBody) : TemplateRequestHandler<CreatePurchaseResponse>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<CreatePurchaseResponse>> {
        val service = NetworkService.paymentService(context)
        return service.createPurchase(createPurchaseBody)
    }
}