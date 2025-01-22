package com.air_wheelly.wheelly.domain.statistics

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.statistics.LastMonthResponse
import hr.air_wheelly.ws.models.responses.statistics.NumberOfRentsPerCarResponse
import hr.air_wheelly.ws.request_handlers.StatisticsGetRentsPerCarRequestHandler
import hr.air_wheelly.ws.request_handlers.StatisticsLastMonthRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state
    val tabOptions = listOf("Per Car", "Last Month")

    fun fetchNumberOfListingsPerCar() {
        val handler = StatisticsGetRentsPerCarRequestHandler(context)

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            handler.sendRequest(
                object : ResponseListener<List<NumberOfRentsPerCarResponse>> {
                    override fun onSuccessfulResponse(response: SuccessfulResponseBody<List<NumberOfRentsPerCarResponse>>) {
                        _state.value = _state.value.copy(
                            numberOfRentsPerCar = response.result,
                            isLoading = false,
                        )
                        Log.d("STATISTICS", "Result: " + response.result.toString())
                    }

                    override fun onErrorResponse(response: ErrorResponseBody) {
                        _state.value = _state.value.copy(
                            errorMessage = response.error_message,
                            isLoading = false
                        )
                        Log.d("STATISTICS", "Error: " + response.error_message)
                    }

                    override fun onNetworkFailure(t: Throwable) {
                        _state.value = _state.value.copy(
                            errorMessage = "Network failure, please try again later",
                            isLoading = false
                        )
                        Log.d("STATISTICS", "Error: " + t.cause)
                    }
                }
            )
        }
    }

    fun fetchLastMonth() {
        val handler = StatisticsLastMonthRequestHandler(context)

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                errorMessage = null
            )

            handler.sendRequest(
                object : ResponseListener<LastMonthResponse> {
                    override fun onSuccessfulResponse(response: SuccessfulResponseBody<LastMonthResponse>) {
                        _state.value = _state.value.copy(
                            lastMonth = response.result,
                            isLoading = false,
                        )
                        Log.d("STATISTICS", "Result: " + response.result.toString())
                    }

                    override fun onErrorResponse(response: ErrorResponseBody) {
                        _state.value = _state.value.copy(
                            errorMessage = response.error_message,
                            isLoading = false
                        )
                        Log.d("STATISTICS", "Error: " + response.error_message)
                    }

                    override fun onNetworkFailure(t: Throwable) {
                        _state.value = _state.value.copy(
                            errorMessage = "Network failure, please try again later",
                            isLoading = false
                        )
                        Log.d("STATISTICS", "Error: " + t.cause)
                    }
                }
            )
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(
            errorMessage = null
        )
    }

    fun selectTab(index: Int) {
        _state.value = _state.value.copy(
            selectedTabIndex = index
        )
        fetchTabData(index)
    }

    fun fetchTabData(index: Int) {
        if (state.value.numberOfRentsPerCar.isEmpty() && index == 0) {
            fetchNumberOfListingsPerCar()
        } else if (state.value.lastMonth == null && index == 1) {
            fetchLastMonth()
        }
    }
}