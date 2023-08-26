package com.droidcon.weatherstation.ui.screens.home

import com.droidcon.weatherstation.api_response.CurrentWeatherResponse

data class HomeUiState(
    val currentWeatherResponse: CurrentWeatherResponse
)