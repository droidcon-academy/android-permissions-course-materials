package com.droidcon.weatherstation.api_response

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("main") val main: Main,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("name") val locationName: String
)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val condition: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
)

data class Wind(
    @SerializedName("speed") val speed: Double
)