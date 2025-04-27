package com.pegio.splash.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.splash.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onUserNotAuthenticated: () -> Unit,
    onRegistrationIncomplete: () -> Unit,
    onUserAuthenticatedAndRegistrationComplete: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val startTime = remember { System.currentTimeMillis() }

    CollectLatestEffect(viewModel.uiEffect) { effect ->

        val elapsedTime = System.currentTimeMillis() - startTime
        if (elapsedTime < 1000) {
            delay(timeMillis = 1000 - elapsedTime)
        }

        when (effect) {
            SplashUiEffect.NavigateToHome -> onUserAuthenticatedAndRegistrationComplete()
            SplashUiEffect.NavigateToAuth -> onUserNotAuthenticated()
            SplashUiEffect.NavigateToRegister -> onRegistrationIncomplete()
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.feature_splash_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun SplashPreview() {
    SplashContent()
}