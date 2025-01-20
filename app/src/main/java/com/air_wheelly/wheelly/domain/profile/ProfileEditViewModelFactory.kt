package com.air_wheelly.wheelly.domain.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileEditViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileEditViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}