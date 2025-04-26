package com.pegio.auth.presentation.screen.register.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.auth.presentation.screen.register.RegisterScreen
import com.pegio.common.presentation.util.popNavigate
import kotlinx.serialization.Serializable

@Serializable
data object RegisterRoute

fun NavController.navigateToRegister() = popNavigate(route = RegisterRoute)

fun NavGraphBuilder.registerScreen(
    onRegisterSuccess: () -> Unit,
) {
    composable<RegisterRoute> {
        RegisterScreen(
            onRegisterSuccess = onRegisterSuccess
        )
    }
}