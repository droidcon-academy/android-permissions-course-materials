package com.droidcon.weatherstation.ui


sealed class Screens(val route: String) {
    object Home : Screens("home_screen")
    object Splash : Screens("splash_screen")
    object LocationRequest : Screens("location_request_screen")
}