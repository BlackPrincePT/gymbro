package com.pegio.gymbro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pegio.auth.presentation.screen.auth.navigation.authScreen
import com.pegio.auth.presentation.screen.auth.navigation.navigateToAuth
import com.pegio.auth.presentation.screen.register.RegisterScreen
import com.pegio.gymbro.navigation.route.MainRoute
import com.pegio.gymbro.navigation.route.RegisterRoute
import com.pegio.gymbro.navigation.route.SplashRoute
import com.pegio.gymbro.navigation.route.navigateToMain
import com.pegio.gymbro.navigation.route.navigateToRegister
import com.pegio.splash.presentation.splash.SplashScreen

@Composable
fun EntryNavigationHost(
    navController: NavHostController,
    mainNavigationHost: @Composable () -> Unit
) {
    NavHost(navController = navController, startDestination = SplashRoute) {

        composable<SplashRoute> {
            SplashScreen(
                onUserNotAuthenticated = navController::navigateToAuth,
                onRegistrationIncomplete = navController::navigateToRegister,
                onUserAuthenticatedAndRegistrationComplete = navController::navigateToMain
            )
        }

        authScreen(
            navigateToHome = navController::navigateToMain,
            navigateToRegister = navController::navigateToRegister
        )

        composable<RegisterRoute> {
            RegisterScreen(
                onRegisterSuccess = navController::navigateToMain
            )
        }

        composable<MainRoute> {
            mainNavigationHost()
        }
    }
}