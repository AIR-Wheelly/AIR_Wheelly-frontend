package com.air_wheelly.wheelly.domain.model

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

class ProfileEditHandler(private val context: Context) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> = _userProfile

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchUserProfile()
    }

    // Fetch user profile
    fun fetchUserProfile() {
        val handler = ProfileEditRequestHandler(context)

        viewModelScope.launch {
            handler.sendRequest(object : ResponseListener<UserProfileResponse> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<UserProfileResponse>) {
                    _userProfile.value = response.result
                    _errorMessage.value = null
                    Log.d("ProfileScreen", "User profile fetched: ${_userProfile.value}")

                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _errorMessage.value = response.error_message
                }

                override fun onNetworkFailure(t: Throwable) {
                    _errorMessage.value = "Network failure: ${t.message}"
                }
            })
        }
    }

    // Update user profile
    fun updateProfile(updateProfileRequest: UpdateProfileRequest) {
        val handler = UpdateProfileRequestHandler(context, updateProfileRequest)

        viewModelScope.launch {
            handler.sendRequest(object : ResponseListener<UpdateProfileResponse> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<UpdateProfileResponse>) {
                    _errorMessage.value = null // Clear error message on success
                    // Optionally, refresh user profile or notify success
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    _errorMessage.value = response.error_message
                }

                override fun onNetworkFailure(t: Throwable) {
                    _errorMessage.value = "Network failure: ${t.message}"
                }
            })
        }
    }

}
