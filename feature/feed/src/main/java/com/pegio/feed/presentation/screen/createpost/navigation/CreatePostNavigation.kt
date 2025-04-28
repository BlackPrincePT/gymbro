package com.pegio.feed.presentation.screen.createpost.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.core.NavigationKeys.SELECTED_WORKOUT_KEY
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.createpost.CreatePostScreen
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRoute(val shouldOpenGallery: Boolean)

fun NavController.navigateToCreatePost(shouldOpenGallery: Boolean) =
    navigate(CreatePostRoute(shouldOpenGallery))

fun NavGraphBuilder.createPostScreen(
    onDismiss: () -> Unit,
    onChooseWorkoutClick: (Boolean) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
) {
    composable<CreatePostRoute>(
        enterTransition = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            )
        },
        exitTransition = {
            slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) { navBackStackEntry ->

        val selectedWorkoutId = navBackStackEntry.savedStateHandle.get<String>(SELECTED_WORKOUT_KEY)

        CreatePostScreen(
            selectedWorkoutId = selectedWorkoutId,
            onDismiss = onDismiss,
            onChooseWorkoutClick = onChooseWorkoutClick,
            onSetupTopBar = onSetupTopBar,
            onShowSnackbar = onShowSnackbar
        )
    }
}