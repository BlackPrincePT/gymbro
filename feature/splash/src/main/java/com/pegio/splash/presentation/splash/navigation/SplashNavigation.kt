package com.pegio.splash.presentation.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.splash.presentation.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute

fun NavGraphBuilder.splashScreen(
    onUserNotAuthenticated: () -> Unit,
    onRegistrationIncomplete: () -> Unit,
    onUserAuthenticatedAndRegistrationComplete: () -> Unit,
) {
    composable<SplashRoute> {
        SplashScreen(
            onUserNotAuthenticated = onUserNotAuthenticated,
            onRegistrationIncomplete = onRegistrationIncomplete,
            onUserAuthenticatedAndRegistrationComplete = onUserAuthenticatedAndRegistrationComplete
        )
    }
}