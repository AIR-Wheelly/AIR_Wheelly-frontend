package hr.air_wheelly.core.network

import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody

interface ResponseListener<T>  {
    fun onSuccessfulResponse(response: SuccessfulResponseBody<T>)
    fun onErrorResponse(response: ErrorResponseBody)
    fun onNetworkFailure(t: Throwable)
}