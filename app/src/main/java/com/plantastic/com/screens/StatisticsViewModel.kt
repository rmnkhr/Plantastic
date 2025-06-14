package com.plantastic.com.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StatisticsViewModel : ViewModel() {

    private val _totalWaterings = MutableStateFlow(123)
    val totalWaterings: StateFlow<Int> = _totalWaterings.asStateFlow()

    private val _successfulRepottings = MutableStateFlow(15)
    val successfulRepottings: StateFlow<Int> = _successfulRepottings.asStateFlow()

    private val _averageWateringInterval = MutableStateFlow("5 days")
    val averageWateringInterval: StateFlow<String> = _averageWateringInterval.asStateFlow()

    // In a real app, you might have functions to load/refresh this data.
    // For this subtask, dummy data is initialized directly.
}
