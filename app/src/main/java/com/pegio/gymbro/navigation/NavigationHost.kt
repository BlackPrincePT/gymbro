package com.pegio.gymbro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pegio.aichat.presentation.screen.aichat.navigation.aiChatScreen
import com.pegio.aichat.presentation.screen.aichat.navigation.navigateToAiChat
import com.pegio.auth.presentation.screen.auth.navigation.authScreen
import com.pegio.auth.presentation.screen.auth.navigation.navigateToAuth
import com.pegio.auth.presentation.screen.register.navigation.navigateToRegister
import com.pegio.auth.presentation.screen.register.navigation.registerScreen
import com.pegio.common.presentation.core.NavigationKeys.SELECTED_WORKOUT_KEY
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
import com.pegio.settings.presentation.screen.account.navigation.accountScreen
import com.pegio.settings.presentation.screen.settings.navigation.settingsScreen
import com.pegio.splash.presentation.splash.navigation.SplashRoute
import com.pegio.splash.presentation.splash.navigation.splashScreen
import com.pegio.workout.presentation.screen.userworkouts.navigation.navigateToUsersWorkouts
import com.pegio.workout.presentation.screen.userworkouts.navigation.userWorkoutsScreen
import com.pegio.workout.presentation.screen.workout.navigation.navigateToWorkout
import com.pegio.workout.presentation.screen.workout.navigation.workoutScreen
import com.pegio.workout.presentation.screen.workoutcreation.navigation.navigateToWorkoutCreation
import com.pegio.workout.presentation.screen.workoutcreation.navigation.workoutCreationScreen
import com.pegio.workout.presentation.screen.workoutplan.navigation.workoutPlanScreen

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


        splashScreen(
            onUserNotAuthenticated = navController::navigateToAuth,
            onRegistrationIncomplete = navController::navigateToRegister,
            onUserAuthenticatedAndRegistrationComplete = navController::popNavigateToFeed
        )


        authScreen(
            onAuthSuccess = navController::popNavigateToFeed,
            onRegistrationRequired = navController::navigateToRegister,
            onShowSnackbar = onShowSnackbar
        )

        registerScreen(
            onRegisterSuccess = navController::popNavigateToFeed
        )


        // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


        aiChatScreen(
            onBackClick = navController::navigateUp,
            onSetupTopBar = onSetupAppBar,
            onShowSnackbar = onShowSnackbar
        )


        // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


        accountScreen(
            onBackClick = navController::navigateUp,
            onSetupTopBar = onSetupAppBar
        )

        settingsScreen(
            onBackClick = navController::navigateUp,
            onSetupTopBar = onSetupAppBar
        )


        // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\

        workoutPlanScreen(
            onBackClick = navController::navigateUp,
            onShowSnackbar = onShowSnackbar,
            onStartWorkout = { workoutId ->
                navController.navigateToWorkout(workoutId)
            },
            onSetupTopBar = onSetupAppBar,
        )


        workoutScreen(
            onBackClick = navController::navigateUp,
            onShowSnackbar = onShowSnackbar,
            onSetupTopBar = onSetupAppBar,
        )

        workoutCreationScreen(
            onBackClick = navController::navigateUp,
            onShowSnackbar = onShowSnackbar,
            onSetupTopBar = onSetupAppBar,
        )

        userWorkoutsScreen(
            onBackClick = { workoutId ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(SELECTED_WORKOUT_KEY, workoutId)

                navController.navigateUp()
            },
            onShowSnackbar = onShowSnackbar,
            onSetupTopBar = onSetupAppBar,
            onStartWorkout = navController::navigateToWorkout,
            onCreateWorkoutClick = navController::navigateToWorkoutCreation
        )

        // <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> <*> \\


        feedScreen(
            onCreatePostClick = navController::navigateToCreatePost,
            onShowPostDetails = navController::navigateToPostDetails,
            onPostAuthorClick = navController::navigateToProfile,
            onPostWorkoutClick = navController::navigateToWorkout,
            onAskGymBroClick = navController::navigateToAiChat,
            onChatClick = navController::navigateToAiChat,
            onOpenDrawerClick = dynamicallyOpenDrawer,
            onShowSnackbar = onShowSnackbar,
            onSetupTopBar = onSetupAppBar
        )

        createPostScreen(
            onDismiss = navController::navigateUp,
            onChooseWorkoutClick = navController::navigateToUsersWorkouts,
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
            onPostWorkoutClick = navController::navigateToWorkout,
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