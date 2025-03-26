package com.pegio.gymbro.presentation.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pegio.gymbro.presentation.core.Route
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val startTime = remember { System.currentTimeMillis() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->

            val elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime < 1000) {
                delay(timeMillis = 1000 - elapsedTime)
            }

            when (effect) {
                SplashUiEffect.NavigateToHome -> navController.navigate(Route.HomeScreen)
                SplashUiEffect.NavigateToAuth -> navController.navigate(Route.AuthScreen)
                SplashUiEffect.NavigateToRegister -> navController.navigate(Route.RegisterScreen)
            }
        }
    }

    Splash()
}

@Composable
private fun Splash() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        Text(
            text = "GymBro",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp)
        )

        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = "Splash Logo",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .size(128.dp)
        )
    }
}

@Preview
@Composable
private fun SplashPreview() {
    Splash()
}