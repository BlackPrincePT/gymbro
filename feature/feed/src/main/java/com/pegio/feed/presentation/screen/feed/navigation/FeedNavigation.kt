package com.pegio.feed.presentation.screen.feed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.popNavigate
import com.pegio.feed.presentation.screen.feed.FeedScreen
import kotlinx.serialization.Serializable

@Serializable
data object FeedRoute

fun NavController.popNavigateToFeed() = popNavigate(route = FeedRoute)

fun NavGraphBuilder.feedScreen(
    onCreatePostClick: (Boolean) -> Unit,
    onShowPostDetails: (String) -> Unit,
    onPostAuthorClick: (String) -> Unit,
    onChatClick: () -> Unit,
    onOpenDrawerClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {
    composable<FeedRoute> {
        FeedScreen(
            onCreatePostClick = onCreatePostClick,
            onShowPostDetails = onShowPostDetails,
            onUserProfileClick = onPostAuthorClick,
            onChatClick = onChatClick,
            onOpenDrawerClick = onOpenDrawerClick,
            onSetupTopBar = onSetupTopBar
        )
    }
}