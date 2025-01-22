package com.air_wheelly.wheelly.domain.statistics

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StatisticsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}