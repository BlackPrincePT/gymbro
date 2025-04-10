package com.pegio.gymbro.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pegio.gymbro.presentation.activity.TopBarState
import com.pegio.gymbro.presentation.navigation.route.AccountRoute
import com.pegio.gymbro.presentation.navigation.route.AiChatRoute
import com.pegio.gymbro.presentation.navigation.route.CreatePostRoute
import com.pegio.gymbro.presentation.navigation.route.HomeRoute
import com.pegio.gymbro.presentation.navigation.route.WorkoutPlanRoute
import com.pegio.gymbro.presentation.navigation.route.navigateToAiChat
import com.pegio.gymbro.presentation.navigation.route.navigateToCreatePost
import com.pegio.gymbro.presentation.screen.account.AccountScreen
import com.pegio.gymbro.presentation.screen.ai_chat.AiChatScreen
import com.pegio.gymbro.presentation.screen.createpost.CreatePostScreen
import com.pegio.gymbro.presentation.screen.home.HomeScreen
import com.pegio.gymbro.presentation.screen.workout_plan.WorkoutPlanScreen

@Composable
fun MainNavigationHost(
    navController: NavHostController,
    onSetupAppBar: (TopBarState) -> Unit,
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
                onSetupTopBar = onSetupAppBar
            )
        }
    }
}