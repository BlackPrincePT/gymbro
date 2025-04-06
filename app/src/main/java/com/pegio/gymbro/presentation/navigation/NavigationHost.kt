package com.pegio.gymbro.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pegio.gymbro.presentation.navigation.route.AccountRoute
import com.pegio.gymbro.presentation.navigation.route.AiChatRoute
import com.pegio.gymbro.presentation.navigation.route.AuthRoute
import com.pegio.gymbro.presentation.navigation.route.CreatePost
import com.pegio.gymbro.presentation.navigation.route.HomeRoute
import com.pegio.gymbro.presentation.navigation.route.RegisterRoute
import com.pegio.gymbro.presentation.navigation.route.SplashRoute
import com.pegio.gymbro.presentation.navigation.route.navigateToAccount
import com.pegio.gymbro.presentation.navigation.route.navigateToAiChat
import com.pegio.gymbro.presentation.navigation.route.navigateToAuth
import com.pegio.gymbro.presentation.navigation.route.navigateToCreatePost
import com.pegio.gymbro.presentation.navigation.route.navigateToHome
import com.pegio.gymbro.presentation.navigation.route.navigateToRegister
import com.pegio.gymbro.presentation.screen.account.AccountScreen
import com.pegio.gymbro.presentation.screen.ai_chat.AiChatScreen
import com.pegio.gymbro.presentation.screen.auth.AuthScreen
import com.pegio.gymbro.presentation.screen.createpost.CreatePostScreen
import com.pegio.gymbro.presentation.screen.home.HomeScreen
import com.pegio.gymbro.presentation.screen.register.RegisterScreen
import com.pegio.gymbro.presentation.screen.splash.SplashScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashRoute) {

        composable<SplashRoute> {
            SplashScreen(
                onUserNotAuthenticated = navController::navigateToAuth,
                onRegistrationIncomplete = navController::navigateToRegister,
                onUserAuthenticatedAndRegistrationComplete = navController::navigateToHome
            )
        }

        composable<AuthRoute> {
            AuthScreen(
                onAuthSuccessAndRegistrationComplete = navController::navigateToHome,
                onAuthSuccessButRegistrationIncomplete = navController::navigateToRegister
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                onRegisterSuccess = navController::navigateToHome
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                onChatClick = navController::navigateToAiChat,
                onAccountClick = navController::navigateToAccount,
                onSignOutSuccess = navController::navigateToAuth,
                onCreatePostClick = navController::navigateToCreatePost
            )
        }

        composable<AccountRoute> {
            AccountScreen(onBackClick = navController::navigateUp)
        }

        composable<AiChatRoute> {
            AiChatScreen(onBackClick = navController::navigateUp)
        }

        composable<CreatePost> {
            CreatePostScreen(onDismiss = navController::navigateUp)
        }
    }
}