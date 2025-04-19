package com.pegio.gymbro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pegio.aichat.presentation.screen.aichat.AiChatScreen
import com.pegio.auth.presentation.screen.auth.navigation.authScreen
import com.pegio.auth.presentation.screen.auth.navigation.navigateToAuth
import com.pegio.auth.presentation.screen.register.RegisterScreen
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.createpost.navigation.createPostScreen
import com.pegio.feed.presentation.screen.createpost.navigation.navigateToCreatePost
import com.pegio.feed.presentation.screen.feed.navigation.feedScreen
import com.pegio.feed.presentation.screen.feed.navigation.popNavigateToFeed
import com.pegio.feed.presentation.screen.postdetails.navigation.navigateToPostDetails
import com.pegio.feed.presentation.screen.postdetails.navigation.postDetailsScreen
import com.pegio.gymbro.navigation.route.AccountRoute
import com.pegio.gymbro.navigation.route.AiChatRoute
import com.pegio.gymbro.navigation.route.RegisterRoute
import com.pegio.gymbro.navigation.route.SplashRoute
import com.pegio.gymbro.navigation.route.WorkoutPlanRoute
import com.pegio.gymbro.navigation.route.navigateToAiChat
import com.pegio.gymbro.navigation.route.navigateToRegister
import com.pegio.settings.presentation.screen.AccountScreen
import com.pegio.splash.presentation.splash.SplashScreen
import com.pegio.workout.presentation.screen.workout_plan.WorkoutPlanScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    onSetupAppBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    dynamicallyOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier
    ) {

        composable<SplashRoute> {
            SplashScreen(
                onUserNotAuthenticated = navController::navigateToAuth,
                onRegistrationIncomplete = navController::navigateToRegister,
                onUserAuthenticatedAndRegistrationComplete = navController::popNavigateToFeed
            )
        }

        authScreen(
            onAuthSuccess = navController::popNavigateToFeed,
            onRegistrationRequired = navController::navigateToRegister,
            onShowSnackbar = onShowSnackbar
        )

        composable<RegisterRoute> {
            RegisterScreen(
                onRegisterSuccess = navController::popNavigateToFeed
            )
        }

        composable<AccountRoute> {
            AccountScreen(
                onBackClick = navController::navigateUp,
                onSetupTopBar = onSetupAppBar
            )
        }

        composable<AiChatRoute> {
            AiChatScreen(
                onBackClick = navController::navigateUp,
                onSetupTopBar = onSetupAppBar
            )
        }

        composable<WorkoutPlanRoute> {
            WorkoutPlanScreen(
                onBackClick = navController::navigateUp,
                onInfoClick = navController::navigateToAiChat,
                onSetupTopBar = onSetupAppBar,
            )
        }

        // ========= Feed ========= \\

        feedScreen(
            onCreatePostClick = navController::navigateToCreatePost,
            onShowPostDetails = navController::navigateToPostDetails,
            onChatClick = navController::navigateToAiChat,
            onOpenDrawerClick = dynamicallyOpenDrawer,
            onSetupTopBar = onSetupAppBar
        )

        createPostScreen(
            onDismiss = navController::navigateUp,
            onSetupTopBar = onSetupAppBar,
            onShowSnackbar = onShowSnackbar
        )

        postDetailsScreen(
            onBackClick = navController::navigateUp,
            onSetupTopBar = onSetupAppBar
        )
    }
}