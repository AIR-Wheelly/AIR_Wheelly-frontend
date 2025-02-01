package com.air_wheelly.wheelly.domain.reservation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.responses.CarListResponse
import hr.air_wheelly.ws.models.responses.CarListingResponse
import hr.air_wheelly.ws.models.responses.CarLocationResponse
import hr.air_wheelly.ws.models.responses.car.AllManufacturers
import hr.air_wheelly.ws.models.responses.car.CarLocationBody
import hr.air_wheelly.ws.models.responses.car.CarModel
import hr.air_wheelly.ws.models.responses.car.NewCarBody
import hr.air_wheelly.ws.request_handlers.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class CarViewModel(
    private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(CarState())
    val state: StateFlow<CarState> = _state

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    init {
        fetchManufacturers()
        fetchFuelTypes()
    }

    private fun fetchManufacturers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            val handler = ManufacturerRequestHandler(context)
            handler.sendRequest(object : ResponseListener<Array<AllManufacturers>> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<Array<AllManufacturers>>) {
                    _state.value = _state.value.copy(
                        manufacturers = response.result.toList(),
                        isLoading = false
                    )
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    logError(response.error_message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    logError("Network failure")
                }
            })
        }
    }

    fun fetchModels(manufacturerId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            val handler = ModelRequestHandler(context, manufacturerId)
            handler.sendRequest(object : ResponseListener<Array<CarModel>> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<Array<CarModel>>) {
                    _state.value = _state.value.copy(
                        models = response.result.toList(),
                        isLoading = false
                    )
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _state.value = _state.value.copy(
                        errorMessage = "Error while fetching car",
                        isLoading = false
                    )
                    logError("Network failure")
                }

                override fun onNetworkFailure(t: Throwable) {
                    _state.value = _state.value.copy(
                        errorMessage = "Network error, please try again later",
                        isLoading = false
                    )
                    logError("Network failure")
                }
            })
        }
    }

    private fun fetchFuelTypes() {
        viewModelScope.launch {
            val handler = FuelTypeRequestHandler(context)
            handler.sendRequest(object : ResponseListener<Array<String>> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<Array<String>>) {
                    _state.value = _state.value.copy(
                        fuelTypes = response.result.toList(),
                        isLoading = false
                    )
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _state.value = _state.value.copy(
                        errorMessage = "Error while fetching car",
                        isLoading = false
                    )
                    logError(response.error_message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    _state.value = _state.value.copy(
                        errorMessage = "Network error, please try again later",
                        isLoading = false
                    )
                    logError("Network failure")
                }
            })
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Location) -> Unit, onError: (String) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
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
                println("TESTING" + response.result)
                onLocationIdReceived(response.result.locationId)
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                _state.value = _state.value.copy(
                    errorMessage = response.error_message,
                    isLoading = false
                )
                logError(response.error_message)
            }

            override fun onNetworkFailure(t: Throwable) {
                _state.value = _state.value.copy(
                    errorMessage = "Network failure, please try again later",
                    isLoading = false
                )
                logError("Network failure")
            }
        })
    }

    fun createCarListing(newCarBody: NewCarBody, onSuccess: (CarListingResponse) -> Unit, onError: (String) -> Unit) {
        val handler = CreateCarRequestHandler(context, newCarBody)
        handler.sendRequest(object : ResponseListener<CarListingResponse> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<CarListingResponse>) {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        successMessage = "Car was Successfully listed"
                    )
                    onSuccess(response.result)
                }
                Log.d("CREATECARLISTING", "Success creating car listing")
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        errorMessage = "Error, please try again later"
                    )
                    onError(response.error_message)
                }
                Log.d("CREATECARLISTING", "OnErrorResponse ${response.error_message}")
            }

            override fun onNetworkFailure(t: Throwable) {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        errorMessage = "Network failure, please try again later"
                    )
                    onError("Network failure")
                }
                Log.d("CREATECARLISTING", t.cause.toString())
            }
        })
    }

    fun getCarDetails(carId: String, onCarDetailsFetched: (CarListResponse) -> Unit) {
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

    fun uploadCarImages(listingId: String, imageUris: List<Uri>, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val contentResolver = context.contentResolver
        val files = imageUris.mapIndexedNotNull { index, uri ->
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                bytes?.let {
                    val requestFile = it.toRequestBody("image/*".toMediaTypeOrNull())
                    val fileName = "image_$index.jpg"
                     MultipartBody.Part.createFormData("files", fileName, requestFile)
                }
            } catch (e: Exception) {
                Log.e("UPLOAD", "Error processing image: ${e.localizedMessage}")
                null
            }
        }

        if (files.isEmpty()) {
            onError("No valid images selected")
            return
        }

        val listingIdRequestBody = listingId.toRequestBody("text/plain".toMediaTypeOrNull())

        val handler = UploadCarImagesRequestHandler(context, listingIdRequestBody, files)
        handler.sendRequest(object : ResponseListener<Unit> {
            override fun onSuccessfulResponse(response: SuccessfulResponseBody<Unit>) {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        successMessage = "Images uploaded successfully!"
                    )
                    onSuccess()
                }
            }

            override fun onErrorResponse(response: ErrorResponseBody) {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        errorMessage = "Failed to upload images"
                    )
                    onError(response.error_message)
                }
            }

            override fun onNetworkFailure(t: Throwable) {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        errorMessage = "Network failure, please try again later"
                    )
                    onError("Network failure")
                }
            }
        })
    }

    private fun logError(message: String) {
        Log.d("ERROR", message)
    }
}