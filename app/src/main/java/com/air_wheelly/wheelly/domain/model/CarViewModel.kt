import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.models.responses.car.CarModel
import hr.air_wheelly.ws.request_handlers.FuelTypeRequestHandler
import hr.air_wheelly.ws.request_handlers.ManufacturerRequestHandler
import hr.air_wheelly.ws.request_handlers.ModelRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarViewModel(private val context: Context) : ViewModel() {
    private val _manufacturers = MutableStateFlow<List<AllManufacturers>>(emptyList())
    val manufacturers: StateFlow<List<AllManufacturers>> = _manufacturers

    private val _models = MutableStateFlow<List<CarModel>>(emptyList())
    val models: StateFlow<List<CarModel>> = _models

    private val _fuelTypes = MutableStateFlow<List<String>>(emptyList())
    val fuelTypes: StateFlow<List<String>> = _fuelTypes

    init {
        fetchManufacturers()
        fetchFuelTypes()
    }

    private fun fetchManufacturers() {
        val handler = ManufacturerRequestHandler(context)
        handler.sendRequest(object : ResponseListener<Array<AllManufacturers>> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<Array<AllManufacturers>>) {
                viewModelScope.launch {
                    _manufacturers.value = response.result.toList()
                }
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.d("ERROR", response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                Log.d("ERROR", "Network failure")
            }
        })
    }

    fun fetchModels(manufacturerId: String) {
        val handler = ModelRequestHandler(context, manufacturerId)
        handler.sendRequest(object : ResponseListener<Array<CarModel>> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<Array<CarModel>>) {
                viewModelScope.launch {
                    _models.value = response.result.toList()
                }
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.d("ERROR", response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                Log.d("ERROR", "Network failure")
            }
        })
    }

    private fun fetchFuelTypes() {
        val handler = FuelTypeRequestHandler(context)
        handler.sendRequest(object : ResponseListener<Array<String>> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<Array<String>>) {
                viewModelScope.launch {
                    _fuelTypes.value = response.result.toList()
                }
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                Log.d("ERROR", response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                Log.d("ERROR", "Network failure")
            }
        })
    }
}