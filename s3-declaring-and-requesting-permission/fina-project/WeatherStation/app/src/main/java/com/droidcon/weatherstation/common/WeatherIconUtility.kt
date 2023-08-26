package com.droidcon.weatherstation.common

object WeatherIconUtility {
    fun getIconUrl(iconId: String): String {
        return "https://openweathermap.org/img/wn/$iconId@4x.png"
    }
}