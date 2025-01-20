package com.air_wheelly.wheelly.domain.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.UpdateProfileRequest
import hr.air_wheelly.ws.models.responses.UpdateProfileResponse
import hr.air_wheelly.ws.models.responses.UserProfileResponse
import hr.air_wheelly.ws.request_handlers.ProfileEditRequestHandler
import hr.air_wheelly.ws.request_handlers.UpdateProfileRequestHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileEditViewModel(
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileEditState())
    val state: StateFlow<ProfileEditState> = _state

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        val handler = ProfileEditRequestHandler(context)

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            handler.sendRequest(object : ResponseListener<UserProfileResponse> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<UserProfileResponse>) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        userProfile = response.result,
                        errorMessage = null
                    )
                    Log.d("PROFILEFETCH", "User profile fetched: ${response.result}")

                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Profile information could not be fetched, please try again later"
                    )
                    Log.d("PROFILEFETCH", "Error: ${response.error_message}")
                }

                override fun onNetworkFailure(t: Throwable) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Network failure, please try again later"
                    )
                    Log.d("PROFILEFETCH", "User profile fetched: ${t.cause}")
                }
            })
        }
    }

    fun updateProfile(updateProfileRequest: UpdateProfileRequest) {
        val handler = UpdateProfileRequestHandler(context, updateProfileRequest)

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            handler.sendRequest(object : ResponseListener<UpdateProfileResponse> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<UpdateProfileResponse>) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                    Log.d("PROFILEEDIT", "User profile fetched: ${response.result}")
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Profile could not be updated, please try again later"
                    )
                    Log.d("PROFILEEDIT", "Error: ${response.error_message}")
                }

                override fun onNetworkFailure(t: Throwable) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Network failure, please try again later"
                    )
                    Log.d("PROFILEEDIT", "User profile fetched: ${t.cause}")
                }
            })
        }
    }

}
