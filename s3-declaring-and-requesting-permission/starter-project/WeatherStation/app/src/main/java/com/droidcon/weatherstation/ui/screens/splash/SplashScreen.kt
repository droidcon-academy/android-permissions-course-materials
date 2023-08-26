package com.droidcon.weatherstation.ui.screens.splash

import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.droidcon.weatherstation.R
import com.droidcon.weatherstation.ui.Screens
import com.droidcon.weatherstation.ui.theme.blueColor

@Composable
fun SplashScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = blueColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.forecast_icon),
            contentDescription = "Forecast Icon",
        )
        Text(
            text = "Weather Station", style = TextStyle(
                color = Color.White,
                fontSize = 22.sp
            )
        )

        val countDownTimer = object : CountDownTimer(1500, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                navController.navigate(Screens.LocationRequest.route){
                    navController.popBackStack()
                }
            }
        }
        DisposableEffect(key1 = "key") {
            countDownTimer.start()
            onDispose {
                countDownTimer.cancel()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplashScreen(navController = rememberNavController())
}