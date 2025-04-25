package com.pegio.feed.presentation.screen.createpost.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.createpost.CreatePostScreen
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRoute(val shouldOpenGallery: Boolean)

fun NavController.navigateToCreatePost(shouldOpenGallery: Boolean) =
    navigate(CreatePostRoute(shouldOpenGallery))

fun NavGraphBuilder.createPostScreen(
    onDismiss: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<CreatePostRoute>(
        enterTransition = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            )
        },
        popExitTransition = {
            slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) {
        CreatePostScreen(
            onDismiss = onDismiss,
            onSetupTopBar = onSetupTopBar,
            onShowSnackbar = onShowSnackbar
        )
    }
}