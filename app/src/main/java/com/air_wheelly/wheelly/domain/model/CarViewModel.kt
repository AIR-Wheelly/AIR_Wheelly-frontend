import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import hr.air_wheelly.core.network.CarListResponse
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarLocationResponse
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.models.responses.car.CarLocationBody
import hr.air_wheelly.ws.models.responses.car.CarModel
import hr.air_wheelly.ws.models.responses.car.NewCarBody
import hr.air_wheelly.ws.request_handlers.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class CarViewModel(private val context: Context) : ViewModel() {
    private val _manufacturers = MutableStateFlow<List<AllManufacturers>>(emptyList())
    val manufacturers: StateFlow<List<AllManufacturers>> = _manufacturers

    private val _models = MutableStateFlow<List<CarModel>>(emptyList())
    val models: StateFlow<List<CarModel>> = _models

    private val _fuelTypes = MutableStateFlow<List<String>>(emptyList())
    val fuelTypes: StateFlow<List<String>> = _fuelTypes

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

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
                logError(response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                logError("Network failure")
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
                logError(response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                logError("Network failure")
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
                logError(response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                logError("Network failure")
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Location) -> Unit, onError: (String) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    onLocationReceived(it)
                } ?: run {
                    onError("Failed to get current location")
                }
            }.addOnFailureListener {
                onError("Failed to fetch location: ${it.localizedMessage}")
            }
        } else {
            onError("Location permission not granted")
        }
    }

    fun sendLocationToApi(location: Location, onLocationIdReceived: (String) -> Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val address = addressList?.get(0)?.getAddressLine(0) ?: "Unknown address"

        val carLocationBody = CarLocationBody(
            latitude = location.latitude,
            longitude = location.longitude,
            adress = address
        )

        val handler = CarLocationRequestHandler(context, carLocationBody)
        handler.sendRequest(object : ResponseListener<CarLocationResponse> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<CarLocationResponse>) {
                println("TESTING"+response.result)
                onLocationIdReceived(response.result.locationId)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                logError(response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                logError("Network failure")
            }
        })
    }

    fun createCarListing(newCarBody: NewCarBody, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val handler = CreateCarRequestHandler(context, newCarBody)
        println("Creating car listing $newCarBody")
        handler.sendRequest(object : ResponseListener<Unit> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<Unit>) {
                viewModelScope.launch {
                    onSuccess()
                }
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                viewModelScope.launch {
                    onError(response.error_message)
                }
            }

            override fun onNetworkFailure(t: Throwable) {
                viewModelScope.launch {
                    onError("Network failure")
                }
            }
        })
    }

    // In CarViewModel.kt
    fun getCarDetails(carId: String, onCarDetailsFetched: (CarListResponse) -> Unit) {
        // Assuming you have a request handler to fetch car details
        val handler = CarByIdRequestHandler(context, carId)
        handler.sendRequest(object : ResponseListener<CarListResponse> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<CarListResponse>) {
                viewModelScope.launch {
                    onCarDetailsFetched(response.result)
                }
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                logError(response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                logError("Network failure")
            }
        })
    }

    private fun logError(message: String) {
        Log.d("ERROR", message)
    }
}