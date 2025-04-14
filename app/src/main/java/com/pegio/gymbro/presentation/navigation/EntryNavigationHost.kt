package com.pegio.gymbro.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pegio.gymbro.presentation.navigation.route.AuthRoute
import com.pegio.gymbro.presentation.navigation.route.MainRoute
import com.pegio.gymbro.presentation.navigation.route.RegisterRoute
import com.pegio.gymbro.presentation.navigation.route.SplashRoute
import com.pegio.gymbro.presentation.navigation.route.navigateToAuth
import com.pegio.gymbro.presentation.navigation.route.navigateToMain
import com.pegio.gymbro.presentation.navigation.route.navigateToRegister
import com.pegio.presentation.screen.auth.AuthScreen
import com.pegio.presentation.screen.register.RegisterScreen
import com.pegio.presentation.splash.SplashScreen

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

        composable<AuthRoute> {
            AuthScreen(
                onAuthSuccessAndRegistrationComplete = navController::navigateToMain,
                onAuthSuccessButRegistrationIncomplete = navController::navigateToRegister
            )
        }

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