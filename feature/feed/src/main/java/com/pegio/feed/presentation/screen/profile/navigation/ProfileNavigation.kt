package com.pegio.feed.presentation.screen.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.profile.ProfileScreen
import com.pegio.model.FollowRecord
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRoute(val userId: String)

fun NavController.navigateToProfile(userId: String) = navigate(ProfileRoute(userId))

private val deepLink1 = navDeepLink<ProfileRoute>(basePath = "gymbro://profile")

fun NavGraphBuilder.profileScreen(
    onBackClick: () -> Unit,
    onFollowRecordClick: (String, FollowRecord.Type) -> Unit,
    onCreatePostClick: (Boolean) -> Unit,
    onPostWorkoutClick: (String) -> Unit,
    onShowPostDetails: (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit
) {
    composable<ProfileRoute>(deepLinks = listOf(deepLink1)) {
        ProfileScreen(
            onBackClick = onBackClick,
            onFollowRecordClick = onFollowRecordClick,
            onCreatePostClick = onCreatePostClick,
            onPostWorkoutClick = onPostWorkoutClick,
            onShowPostDetails = onShowPostDetails,
            onSetupTopBar = onSetupTopBar,
            onShowSnackbar = onShowSnackbar
        )
    }
}