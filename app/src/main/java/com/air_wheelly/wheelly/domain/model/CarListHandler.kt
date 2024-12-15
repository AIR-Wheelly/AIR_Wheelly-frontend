package com.air_wheelly.wheelly.domain.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.request_handlers.CarListRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarListHandler(private val context: Context) : ViewModel() {

    private val _carList = MutableStateFlow<List<CarListResponse>>(emptyList())
    val carList: StateFlow<List<CarListResponse>> = _carList
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchCarList()
    }

     fun fetchCarList() {
        val handler = CarListRequestHandler(context)

        viewModelScope.launch {
            handler.sendRequest(object : ResponseListener<List<CarListResponse>> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<List<CarListResponse>>) {
                    viewModelScope.launch {
                        _carList.value = response.result
                        _errorMessage.value = null
                    }
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    viewModelScope.launch {
                        _errorMessage.value = response.error_message
                    }
                }

                override fun onNetworkFailure(t: Throwable) {
                    viewModelScope.launch {
                        _errorMessage.value = "Network failure: ${t.message}"
                    }
                }
            })
        }
    }
}