package com.pegio.gymbro.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pegio.gymbro.presentation.activity.TopBarState
import com.pegio.gymbro.presentation.navigation.route.AccountRoute
import com.pegio.gymbro.presentation.navigation.route.AiChatRoute
import com.pegio.gymbro.presentation.navigation.route.AuthRoute
import com.pegio.gymbro.presentation.navigation.route.CreatePostRoute
import com.pegio.gymbro.presentation.navigation.route.HomeRoute
import com.pegio.gymbro.presentation.navigation.route.MainRoute
import com.pegio.gymbro.presentation.navigation.route.RegisterRoute
import com.pegio.gymbro.presentation.navigation.route.SplashRoute
import com.pegio.gymbro.presentation.navigation.route.navigateToAccount
import com.pegio.gymbro.presentation.navigation.route.navigateToAiChat
import com.pegio.gymbro.presentation.navigation.route.navigateToAuth
import com.pegio.gymbro.presentation.navigation.route.navigateToCreatePost
import com.pegio.gymbro.presentation.navigation.route.navigateToHome
import com.pegio.gymbro.presentation.navigation.route.navigateToMain
import com.pegio.gymbro.presentation.navigation.route.navigateToRegister
import com.pegio.gymbro.presentation.screen.account.AccountScreen
import com.pegio.gymbro.presentation.screen.ai_chat.AiChatScreen
import com.pegio.gymbro.presentation.screen.auth.AuthScreen
import com.pegio.gymbro.presentation.screen.createpost.CreatePostScreen
import com.pegio.gymbro.presentation.screen.home.HomeScreen
import com.pegio.gymbro.presentation.screen.register.RegisterScreen
import com.pegio.gymbro.presentation.screen.splash.SplashScreen

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