package com.pegio.feed.presentation.screen.postdetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.postdetails.PostDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data class PostDetailsRoute(val postId: String)

fun NavController.navigateToPostDetails(postId: String) = navigate(route = PostDetailsRoute(postId))

private val deepLink1 = navDeepLink<PostDetailsRoute>(basePath = "gymbro://post-details")

fun NavGraphBuilder.postDetailsScreen(
    onBackClick: () -> Unit,
    onUserProfileClick: (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
) {
    composable<PostDetailsRoute>(deepLinks = listOf(deepLink1)) {
        PostDetailsScreen(
            onBackClick = onBackClick,
            onUserProfileClick = onUserProfileClick,
            onSetupTopBar = onSetupTopBar,
            onShowSnackbar = onShowSnackbar
        )
    }
}