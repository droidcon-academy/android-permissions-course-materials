package com.droidcon.weatherstation.api

import com.droidcon.weatherstation.api.networkresponse.NetworkResponse
import com.droidcon.weatherstation.api_response.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): NetworkResponse<CurrentWeatherResponse, Any>
}