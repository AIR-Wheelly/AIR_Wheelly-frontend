package com.air_wheelly.wheelly.domain.car_list

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.core.util.EnumFuelType
import hr.air_wheelly.ws.models.responses.CarListResponse
import hr.air_wheelly.ws.request_handlers.CarListRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarListViewModel(
    private val context: Context
) : ViewModel() {

    private val carListModel = CarListModel()
    private val _state = MutableStateFlow(CarListState())
    val state: StateFlow<CarListState> = _state

     fun fetchCarList() {
        val handler = CarListRequestHandler(context)

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            handler.sendRequest(object : ResponseListener<List<CarListResponse>> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<List<CarListResponse>>) {
                    _state.value = _state.value.copy(
                        carList = response.result,
                        filteredCarList = carListModel.filterCars(
                            response.result,
                            _state.value.selectedFuelType,
                            _state.value.selectedManufacturer,
                            _state.value.selectedYear
                        ),
                        isLoading = false
                    )
                    Log.d("CARLIST", response.result.toString())
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _state.value = _state.value.copy(
                        errorMessage = response.error_message,
                        isLoading = false
                    )
                    Log.d("CARlIST", response.error_message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    _state.value = _state.value.copy(
                        errorMessage = "Network failure, please try again later",
                        isLoading = false
                    )
                    Log.d("CARlIST", t.cause.toString())
                }
            })
        }
    }

    fun applyFilters(fuelType: Set<EnumFuelType>, manufacturer: String, year: Int?) {
        _state.value = _state.value.copy(
            selectedFuelType = fuelType,
            selectedManufacturer = manufacturer,
            selectedYear = year,
            filteredCarList = carListModel.filterCars(
                _state.value.carList,
                fuelType,
                manufacturer,
                year
            )
        )
    }

    fun clearFilters() {
        _state.value = _state.value.copy(
            selectedFuelType = EnumFuelType.values().toSet(),
            selectedManufacturer = "",
            selectedYear = null,
            filteredCarList = carListModel.filterCars(
                _state.value.carList,
                EnumFuelType.values().toSet(),
                "",
                null
            )
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(
            errorMessage = null
        )
    }
}