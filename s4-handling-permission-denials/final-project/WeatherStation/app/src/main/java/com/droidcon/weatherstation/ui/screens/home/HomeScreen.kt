package com.droidcon.weatherstation.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.droidcon.weatherstation.api_response.CurrentWeatherResponse
import com.droidcon.weatherstation.api_response.Main
import com.droidcon.weatherstation.api_response.Weather
import com.droidcon.weatherstation.api_response.Wind
import com.droidcon.weatherstation.common.Center
import com.droidcon.weatherstation.common.WeatherIconUtility
import com.droidcon.weatherstation.common.toWeatherDetailsList
import com.droidcon.weatherstation.data.model.WeatherDetailsItem
import com.droidcon.weatherstation.network.Resource
import com.droidcon.weatherstation.ui.theme.WeatherStationTheme
import com.droidcon.weatherstation.ui.theme.greyBackground
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    state: Resource<HomeUiState>,
    onSaveToStorageClicked: (imageBitmap: ImageBitmap) -> Unit,
    onCaptureError: (error: Throwable) -> Unit,
    onShowNotificationClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = greyBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        when (state) {
            is Resource.Loading -> Center {
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    TodayCard(
                        state.toData()!!.currentWeatherResponse,
                        onSaveToStorageClicked,
                        onCaptureError,
                        onShowNotificationClicked
                    )
                }
            }

            is Resource.Error -> Center {
                Text(text = state.error, style = TextStyle(color = Color.Red))
            }

            else -> {}
        }
    }
}

@Composable
fun TodayCard(
    currentWeather: CurrentWeatherResponse,
    onSaveToStorageClicked: (imageBitmap: ImageBitmap) -> Unit,
    onCaptureError: (error: Throwable) -> Unit,
    onShowNotificationClicked: () -> Unit
) {
    Card(
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95F)
                .padding(32.dp)
        ) {
            LocationWithIcon(currentWeather.locationName)
            Spacer(modifier = Modifier.height(20.dp))
            WeatherCondition(currentWeather.main, currentWeather.weather.first())
            Spacer(modifier = Modifier.height(20.dp))

            val captureController = rememberCaptureController()
            Capturable(controller = captureController, onCaptured = { bitmap, error ->
                if (bitmap != null) {
                    onSaveToStorageClicked(bitmap)
                }
                if (error != null) {
                    onCaptureError(error)
                }
            }) {
                WeatherDetails(currentWeather.toWeatherDetailsList())
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
                onClick = {
                    captureController.capture()
                }) {
                Text(text = "Save to storage as Image")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
                onClick = {
                    onShowNotificationClicked()
                }) {
                Text(text = "Show a notification")
            }
        }
    }
}

@Composable
fun WeatherCondition(mainCondition: Main, weather: Weather) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = WeatherIconUtility.getIconUrl(weather.icon),
                contentDescription = "Weather Condition Icon"
            )
            Box(modifier = Modifier.fillMaxWidth(0.60F)) {
                Text(
                    text = mainCondition.temp.roundToInt().toString(), style = TextStyle(
                        fontSize = 75.sp, fontWeight = FontWeight.W400
                    )
                )
                Text(
                    text = "o", style = TextStyle(
                        textAlign = TextAlign.End, fontWeight = FontWeight.Bold
                    ), modifier = Modifier.fillMaxWidth(0.8F)
                )
            }
        }
        Text(
            text = weather.description.capitalize(), style = TextStyle(
                fontSize = 30.sp, fontWeight = FontWeight.W400
            )
        )
    }
}

@Composable
fun LocationWithIcon(location: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            imageVector = Icons.Filled.LocationOn, contentDescription = "current location icon"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = location, style = TextStyle(
                fontSize = 22.sp, fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun WeatherDetails(weatherDetailsList: List<WeatherDetailsItem>) {
    Column() {
        Text(
            text = "Today Details", style = TextStyle(
                fontWeight = FontWeight.Bold, fontSize = 20.sp
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(weatherDetailsList) { item ->
                WeatherDetails(
                    imageDrawableRes = item.imageDrawableRes, value = item.value, label = item.label
                )
            }
        }
    }
}

@Composable
private fun WeatherDetails(imageDrawableRes: Int, value: String, label: String) {
    Card(
        elevation = 2.dp,
        modifier = Modifier.padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = imageDrawableRes),
                contentDescription = "Condition Image",
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = value, style = TextStyle(
                        fontSize = 22.sp, fontWeight = FontWeight.W800
                    )
                )
                Text(
                    text = label, style = TextStyle(
                        fontSize = 18.sp, fontWeight = FontWeight.W400
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    WeatherStationTheme {
        Surface {
            HomeScreen(
                state = Resource.Success(
                    HomeUiState(
                        CurrentWeatherResponse(
                            weather = listOf(element = Weather(1, "Cloudy", "", "")),
                            main = Main(27.0, 34, 20),
                            wind = Wind(75.0),
                            locationName = "Bengaluru, Karnataka"
                        ),
                    )
                ),
                onSaveToStorageClicked = { },
                onCaptureError = { },
                onShowNotificationClicked = {}
            )
        }
    }
}