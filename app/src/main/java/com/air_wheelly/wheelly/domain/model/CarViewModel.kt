import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.request_handlers.ManufacturerRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarViewModel(private val context: Context) : ViewModel() {
    private val _manufacturers = MutableStateFlow<List<AllManufacturers>>(emptyList())
    val manufacturers: StateFlow<List<AllManufacturers>> = _manufacturers

    init {
        fetchManufacturers()
    }

    private fun fetchManufacturers() {
        val handler = ManufacturerRequestHandler(context)
        handler.sendRequest(object : ResponseListener<Array<AllManufacturers>> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<Array<AllManufacturers>>) {
                viewModelScope.launch {
                    println("Response: ${response.result.toList()}")
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
}