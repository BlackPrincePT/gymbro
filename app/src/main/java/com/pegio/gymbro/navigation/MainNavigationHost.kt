package com.pegio.gymbro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pegio.aichat.presentation.screen.ai_chat.AiChatScreen
import com.pegio.feed.presentation.screen.createpost.CreatePostScreen
import com.pegio.feed.presentation.screen.feed.HomeScreen
import com.pegio.common.presentation.state.TopBarState
import com.pegio.gymbro.navigation.route.AccountRoute
import com.pegio.gymbro.navigation.route.AiChatRoute
import com.pegio.gymbro.navigation.route.CreatePostRoute
import com.pegio.gymbro.navigation.route.HomeRoute
import com.pegio.gymbro.navigation.route.WorkoutPlanRoute
import com.pegio.gymbro.navigation.route.navigateToAiChat
import com.pegio.gymbro.navigation.route.navigateToCreatePost
import com.pegio.settings.presentation.screen.AccountScreen
import com.pegio.workout.presentation.screen.workout_plan.WorkoutPlanScreen

@Composable
fun MainNavigationHost(
    navController: NavHostController,
    onSetupAppBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    dynamicallyOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {

        composable<HomeRoute> {
            HomeScreen(
                onCreatePostClick = navController::navigateToCreatePost,
                onChatClick = navController::navigateToAiChat,
                onDrawerClick = dynamicallyOpenDrawer,
                onSetupTopBar = onSetupAppBar
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

        composable<CreatePostRoute> {
            CreatePostScreen(
                onDismiss = navController::navigateUp,
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
    }
}