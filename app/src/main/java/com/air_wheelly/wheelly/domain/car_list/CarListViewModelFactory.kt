package com.air_wheelly.wheelly.domain.car_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CarListViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarListViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}