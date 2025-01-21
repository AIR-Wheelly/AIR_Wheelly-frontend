package com.air_wheelly.wheelly.domain.payment

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInRequest
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarListResponse
import hr.air_wheelly.ws.request_handlers.CarByIdRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val context: Context
) : ViewModel(){
    private val _state = MutableStateFlow(PaymentState())
    val state: StateFlow<PaymentState> get() = _state

    suspend fun fetchCarDetails(carListingId: String) {
        _state.value = _state.value.copy(isLoading = true)
        val handler = CarByIdRequestHandler(context, carListingId)

        handler.sendRequest(
            object : ResponseListener<CarListResponse> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<CarListResponse>) {
                    _state.value = _state.value.copy(
                        carListingById = response.result,
                        isLoading = false,
                        errorMessage = null
                    )
                    Log.d("CARLISTID", response.result.toString())
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = response.error_message
                    )
                    Log.d("CARLISTID", response.error_message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Network failure!"
                    )
                    Log.d("CARLISTID", t.cause.toString())
                }
            }
        )
    }

    fun launchPayment(dropInClient: DropInClient?, dropInRequest: DropInRequest) {
        if (dropInClient == null) return

        viewModelScope.launch {
            dropInClient.launchDropIn(dropInRequest)
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(
            errorMessage = null
        )
    }
}