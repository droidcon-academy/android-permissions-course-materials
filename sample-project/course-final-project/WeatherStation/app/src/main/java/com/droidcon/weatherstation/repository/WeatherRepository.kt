package com.droidcon.weatherstation.repository

import com.droidcon.weatherstation.api.Api
import com.droidcon.weatherstation.api.networkresponse.NetworkResponse
import com.droidcon.weatherstation.api_response.CurrentWeatherResponse
import com.droidcon.weatherstation.common.Constants
import javax.inject.Inject

class WeatherRepository @Inject constructor() {

    suspend fun getCurrentWeather(
        latitude: Float,
        longitude: Float
    ): NetworkResponse<CurrentWeatherResponse, Any> {
        return Api.weatherServiceApi().getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            units = "metric",
            apiKey = Constants.API_KEY,
        )
    }
}