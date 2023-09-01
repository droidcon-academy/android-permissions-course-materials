package com.droidcon.weatherstation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.droidcon.weatherstation.common.*
import com.droidcon.weatherstation.ui.Screens
import com.droidcon.weatherstation.ui.screens.home.HomeScreen
import com.droidcon.weatherstation.ui.screens.home.HomeViewModel
import com.droidcon.weatherstation.ui.screens.locationrequest.LocationRequestScreen
import com.droidcon.weatherstation.ui.screens.splash.SplashScreen
import com.droidcon.weatherstation.ui.theme.WeatherStationTheme
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherStationTheme {
                val navController = rememberNavController()
                MyApp(navController)
            }
        }
    }

    @Composable
    fun MyApp(navController: NavHostController) {
        val permissionsViewModel = hiltViewModel<PermissionsViewModel>()
        val dialogQueue = permissionsViewModel.visiblePermissionDialogQueue

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    onLocationPermissionGranted(navController)
                } else {
                    //@Todo handle permission denial
                }
            }
        )

        dialogQueue.forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        StoragePermissionTextProvider()
                    }

                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        LocationPermissionTextProvider()
                    }

                    Manifest.permission.POST_NOTIFICATIONS -> {
                        NotificationsPermissionProvider()
                    }

                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
                onDismiss = permissionsViewModel::dismissDialog,
                onOkClick = {
                    permissionsViewModel.dismissDialog()
                    permissionLauncher.launch(permission)
                },
                onGoToAppSettingsClick = {
                    openAppSettings()
                }
            )
        }

        var weatherImageBitmp: ImageBitmap? = null

        val notificationsPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    onNotificationPermissionGranted()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionsViewModel.onPermissionDenied(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            })

        val storagePermissionResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    onStoragePermissionGranted(weatherImageBitmp)
                } else {
                    //@Todo handle permission denial
                }
            })

        val locationPermissionResultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->

                if (isGranted) {
                    onLocationPermissionGranted(navController)
                } else {
                    permissionsViewModel.onPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        )

        NavHost(navController, startDestination = Screens.Splash.route) {
            composable(route = Screens.Splash.route) {
                SplashScreen(navController = navController)
            }
            composable(route = Screens.LocationRequest.route) {
                LocationRequestScreen(onAccessLocationClicked = {
                    locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                })
            }
            composable(
                route = Screens.Home.route.plus("/{latitude}/{longitude}"),
                arguments = listOf(navArgument("latitude") { type = NavType.FloatType },
                    navArgument("longitude") { type = NavType.FloatType })
            ) {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(homeViewModel.uiState.value, onSaveToStorageClicked = { imageBitmap ->
                    weatherImageBitmp = imageBitmap

                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        storagePermissionResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        onStoragePermissionGranted(weatherImageBitmp)
                    }

                }, onCaptureError = {
                    toast("Cannot capture the screen")
                }, onShowNotificationClicked = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        onNotificationPermissionGranted()
                    }
                })
            }
        }
    }

    private fun onStoragePermissionGranted(weatherImageBitmap: ImageBitmap?) {
        weatherImageBitmap?.let {
            // Save image to local storage
            val bitmap = weatherImageBitmap.asAndroidBitmap()
            val savedImageUri = BitmapUtils.saveImageToStorage(
                this, bitmap,
                System.currentTimeMillis().toString()
            )
            if (savedImageUri != null) {
                toast("Image saved to storage!")
            } else {
                toast("Error saving image,")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun onLocationPermissionGranted(navController: NavHostController) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                // Navigate to Home
                navController.navigate(Screens.Home.route.plus("/$latitude/${longitude}"))
            } else {
                toast("Cannot access your location")
            }
        }
    }

    private fun onNotificationPermissionGranted() {
        sendNotification(this)
    }
}
