package com.air_wheelly.wheelly.domain

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInRequest
import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.request_handlers.CarByIdRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val context: Context
) : ViewModel(){
    private val _carListingByID = MutableStateFlow<CarListResponse?>(null)
    val carListingByID: StateFlow<CarListResponse?> get() = _carListingByID

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun fetchCarDetails(carListingId: String) {
        _isLoading.value = true
        val handler = CarByIdRequestHandler(context, carListingId)

        handler.sendRequest(
            object : ResponseListener<CarListResponse> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<CarListResponse>) {
                    _carListingByID.value = response.result
                    _isLoading.value = false
                    Log.d("CARLISTID", response.result.toString())
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _isLoading.value = false
                    Log.d("CARLISTID", response.error_message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    _isLoading.value = false
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
}