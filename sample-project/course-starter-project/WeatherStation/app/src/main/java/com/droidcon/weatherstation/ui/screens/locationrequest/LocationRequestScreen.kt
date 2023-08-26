package com.droidcon.weatherstation.ui.screens.locationrequest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidcon.weatherstation.ui.theme.WeatherStationTheme

@Composable
fun LocationRequestScreen(
    onAccessLocationClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(50.dp),
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Location Icon"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Location Access",
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "The app will need access to your location to fetch weather data.")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onAccessLocationClicked() }) {
            Text(text = "Access Location")
        }
    }
}

@Preview
@Composable
fun LocationRequestPreview(){
    WeatherStationTheme {
        LocationRequestScreen {

        }
    }
}