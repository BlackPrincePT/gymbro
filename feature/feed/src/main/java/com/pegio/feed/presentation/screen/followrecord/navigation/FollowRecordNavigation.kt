package com.pegio.feed.presentation.screen.followrecord.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.feed.presentation.screen.followrecord.FollowRecordScreen
import com.pegio.model.FollowRecord
import kotlinx.serialization.Serializable

@Serializable
data class FollowRecordRoute(val userId: String, val mode: FollowRecord.Type)

fun NavController.navigateToFollowRecord(userId: String, mode: FollowRecord.Type) =
    navigate(FollowRecordRoute(userId, mode))

fun NavGraphBuilder.followRecordScreen(
    onBackClick: () -> Unit,
    onUserProfileClick: (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {
    composable<FollowRecordRoute> {
        FollowRecordScreen(
            onBackClick = onBackClick,
            onUserProfileClick = onUserProfileClick,
            onSetupTopBar = onSetupTopBar
        )
    }
}