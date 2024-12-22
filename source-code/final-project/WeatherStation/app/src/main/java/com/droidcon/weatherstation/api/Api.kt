package com.droidcon.weatherstation.api

import com.droidcon.weatherstation.common.Constants

class Api {
    companion object {
        fun weatherServiceApi(): WeatherApiService {
            return RetroFitClient
                .getClient(Constants.BASE_URL)
                .create(WeatherApiService::class.java)
        }
    }
}