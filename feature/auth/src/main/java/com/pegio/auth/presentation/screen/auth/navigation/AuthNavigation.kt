package com.pegio.auth.presentation.screen.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.auth.presentation.screen.auth.AuthScreen
import com.pegio.common.presentation.util.popNavigate
import kotlinx.serialization.Serializable

@Serializable
internal data object AuthRoute

fun NavController.navigateToAuth() = popNavigate(route = AuthRoute)

fun NavGraphBuilder.authScreen(
    onAuthSuccess: () -> Unit,
    onRegistrationRequired: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable<AuthRoute> {
        AuthScreen(
            onAuthSuccess = onAuthSuccess,
            onRegistrationRequired = onRegistrationRequired,
            onShowSnackbar = onShowSnackbar
        )
    }
}