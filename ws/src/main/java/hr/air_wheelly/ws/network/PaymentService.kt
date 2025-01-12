package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.body.CreatePurchaseBody
import hr.air_wheelly.ws.models.responses.payment.ClientIdResponse
import hr.air_wheelly.ws.models.responses.payment.CreatePurchaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentService {
    @GET("payment/clientId")
    fun getClientId() : Call<SuccessfulResponseBody<ClientIdResponse>>

    @POST("payment/createPurchase")
    fun createPurchase(@Body createPurchaseBody: CreatePurchaseBody) : Call<SuccessfulResponseBody<CreatePurchaseResponse>>
}