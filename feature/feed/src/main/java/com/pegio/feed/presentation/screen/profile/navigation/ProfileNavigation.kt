package com.pegio.feed.presentation.screen.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRoute(val userId: String)

fun NavController.navigateToProfile(userId: String) = navigate(ProfileRoute(userId))

fun NavGraphBuilder.profileScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<ProfileRoute> {
        ProfileScreen(
            onBackClick = onBackClick,
            onSetupTopBar = onSetupTopBar
        )
    }
}

