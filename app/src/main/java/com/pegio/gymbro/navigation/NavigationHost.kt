package com.pegio.gymbro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pegio.aichat.presentation.screen.aichat.AiChatScreen
import com.pegio.auth.presentation.screen.auth.navigation.authScreen
import com.pegio.auth.presentation.screen.auth.navigation.navigateToAuth
import com.pegio.auth.presentation.screen.register.RegisterScreen
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.createpost.navigation.createPostScreen
import com.pegio.feed.presentation.screen.createpost.navigation.navigateToCreatePost
import com.pegio.feed.presentation.screen.feed.navigation.feedScreen
import com.pegio.feed.presentation.screen.feed.navigation.popNavigateToFeed
import com.pegio.feed.presentation.screen.followrecord.navigation.followRecordScreen
import com.pegio.feed.presentation.screen.followrecord.navigation.navigateToFollowRecord
import com.pegio.feed.presentation.screen.postdetails.navigation.navigateToPostDetails
import com.pegio.feed.presentation.screen.postdetails.navigation.postDetailsScreen
import com.pegio.feed.presentation.screen.profile.navigation.navigateToProfile
import com.pegio.feed.presentation.screen.profile.navigation.profileScreen
import com.pegio.gymbro.navigation.route.AccountRoute
import com.pegio.gymbro.navigation.route.AiChatRoute
import com.pegio.gymbro.navigation.route.RegisterRoute
import com.pegio.gymbro.navigation.route.SplashRoute
import com.pegio.gymbro.navigation.route.WorkoutCreationRoute
import com.pegio.gymbro.navigation.route.WorkoutPlanRoute
import com.pegio.gymbro.navigation.route.WorkoutRoute
import com.pegio.gymbro.navigation.route.navigateToAiChat
import com.pegio.gymbro.navigation.route.navigateToRegister
import com.pegio.gymbro.navigation.route.navigateToWorkout
import com.pegio.settings.presentation.screen.AccountScreen
import com.pegio.splash.presentation.splash.SplashScreen
import com.pegio.workout.presentation.screen.workout.WorkoutScreen
import com.pegio.workout.presentation.screen.workout_plan.WorkoutPlanScreen
import com.pegio.workout.presentation.screen.workoutcreation.WorkoutCreationScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    onSetupAppBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
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
                onSetupTopBar = onSetupAppBar,
                onShowSnackbar = onShowSnackbar
            )
        }

        composable<WorkoutPlanRoute> {
            WorkoutPlanScreen(
                onBackClick = navController::navigateUp,
                onInfoClick = navController::navigateToAiChat,
                onShowSnackbar = onShowSnackbar,
                onStartWorkout = { difficulty ->
                    navController.navigateToWorkout(difficulty)
                },
                onSetupTopBar = onSetupAppBar,

            )
        }

        composable<WorkoutRoute> {
            WorkoutScreen(
                onBackClick = navController::navigateUp,
                onShowSnackbar = onShowSnackbar,
                workoutPlanId = it.toRoute<WorkoutRoute>().workoutPlanId,
                onSetupTopBar = onSetupAppBar,
            )
        }

        composable<WorkoutCreationRoute>{
            WorkoutCreationScreen(
                onBackClick = navController::navigateUp,
                onShowSnackbar = onShowSnackbar,
                onSetupTopBar = onSetupAppBar,
            )
        }

        // ========= Feed ========= \\

        feedScreen(
            onCreatePostClick = navController::navigateToCreatePost,
            onShowPostDetails = navController::navigateToPostDetails,
            onPostAuthorClick = navController::navigateToProfile,
            onChatClick = navController::navigateToAiChat,
            onOpenDrawerClick = dynamicallyOpenDrawer,
            onShowSnackbar = onShowSnackbar,
            onSetupTopBar = onSetupAppBar
        )

        createPostScreen(
            onDismiss = navController::navigateUp,
            onSetupTopBar = onSetupAppBar,
            onShowSnackbar = onShowSnackbar
        )

        postDetailsScreen(
            onBackClick = navController::navigateUp,
            onUserProfileClick = navController::navigateToProfile,
            onSetupTopBar = onSetupAppBar,
            onShowSnackbar = onShowSnackbar
        )

        profileScreen(
            onBackClick = navController::navigateUp,
            onFollowRecordClick = navController::navigateToFollowRecord,
            onCreatePostClick = navController::navigateToCreatePost,
            onShowPostDetails = navController::navigateToPostDetails,
            onSetupTopBar = onSetupAppBar,
            onShowSnackbar = onShowSnackbar
        )

        followRecordScreen(
            onBackClick = navController::navigateUp,
            onUserProfileClick = navController::navigateToProfile,
            onSetupTopBar = onSetupAppBar
        )
    }
}