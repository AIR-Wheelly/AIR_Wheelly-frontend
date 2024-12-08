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
                    val body = response.body()
                    if (body != null) {
                        responseListener.onSuccessfulResponse(body)
                    } else {
                        responseListener.onErrorResponse(
                            ErrorResponseBody(
                                success = false,
                                message = "Empty response body",
                                error_code = -1,
                                error_message = "Empty response body"
                            )
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    try {
                        val errorResponse = Gson().fromJson(
                            errorBody,
                            ErrorResponseBody::class.java
                        )
                        responseListener.onErrorResponse(errorResponse)
                    } catch (e: Exception) {
                        responseListener.onErrorResponse(
                            ErrorResponseBody(
                                success = false,
                                message = "Error occurred",
                                error_code = response.code(),
                                error_message = errorBody ?: "Unknown error"
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<SuccessfulResponseBody<T>>, t: Throwable) {
                responseListener.onNetworkFailure(t)
            }
        })
    }

    protected abstract fun getServiceCall(): Call<SuccessfulResponseBody<T>>
}