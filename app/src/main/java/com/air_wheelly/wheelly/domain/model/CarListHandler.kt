package com.air_wheelly.wheelly.domain.model

import android.util.Log
import hr.air_wheelly.core.network.CarListOutcomeListener
import hr.air_wheelly.core.network.ResponseCarList
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.request_handlers.CarListRequestHandler

class CarListHandler {
    suspend fun handleCarList(
        carListOutcomeListener: CarListOutcomeListener
    ) {
        val carListRequestHandler = CarListRequestHandler()

        carListRequestHandler.sendRequest(
            object : ResponseListener<ResponseCarList> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<ResponseCarList>) {

                    try {
                        carListOutcomeListener.onSuccessfulCarListFetch(
                            ResponseCarList(
                                result = response.response.result
                            )
                        )
                    } catch (e: Exception){
                        Log.d("ERROR", "Error kod fetchanja")
                    }
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    Log.d("ERROR", response.error_message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    Log.d("ERROR", "Network failure")
                }
            }
        )
    }
}