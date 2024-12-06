package hr.air_wheelly.ws.request_handlers

import com.google.gson.Gson
import hr.air_wheelly.core.network.RequestHandler
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class TemplateRequestHandler<T> : RequestHandler<T> {
    override fun sendRequest(responseListener: ResponseListener<T>) {
        val serviceCall = getServiceCall()

        serviceCall.enqueue(object : Callback<SuccessfulResponseBody<T>> {
            override fun onResponse(
                call: Call<SuccessfulResponseBody<T>>,
                response: Response<SuccessfulResponseBody<T>>
            ) {
                if (response.isSuccessful) {
                    responseListener.onSuccessfulResponse(response.body() as SuccessfulResponseBody<T>)
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.string(),
                        ErrorResponseBody::class.java
                    )
                    responseListener.onErrorResponse(errorResponse)
                }
            }

            override fun onFailure(call: Call<SuccessfulResponseBody<T>>, t: Throwable) {
                responseListener.onNetworkFailure(t)
            }
        })
    }

    protected abstract fun getServiceCall(): Call<SuccessfulResponseBody<T>>
}