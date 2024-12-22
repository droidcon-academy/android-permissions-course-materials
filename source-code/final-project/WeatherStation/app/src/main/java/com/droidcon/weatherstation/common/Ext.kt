package com.droidcon.weatherstation.common

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.droidcon.weatherstation.R
import com.droidcon.weatherstation.api_response.CurrentWeatherResponse
import com.droidcon.weatherstation.data.model.WeatherDetailsItem
import java.io.File

fun CurrentWeatherResponse.toWeatherDetailsList(): List<WeatherDetailsItem> {
    return listOf(
        WeatherDetailsItem(
            R.drawable.humdiity,
            value = "${this.main.humidity}%", label = "Humidity"
        ),
        WeatherDetailsItem(
            R.drawable.wind, value = "${this.wind.speed} kmh", label = "Wind"
        ),
        WeatherDetailsItem(
            R.drawable.pressure, value = "${this.main.pressure} hPa", label = "Pressure"
        ),
    )
}

fun ComponentActivity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

fun ComponentActivity.toast(message: String) {
    Toast.makeText(
        this, message, Toast.LENGTH_SHORT
    ).show()
}