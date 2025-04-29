package com.pegio.aichat.presentation.screen.aichat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.aichat.presentation.screen.aichat.AiChatScreen
import com.pegio.common.presentation.state.TopBarState
import kotlinx.serialization.Serializable

@Serializable
data class AiChatRoute(val postId: String?)

fun NavController.navigateToAiChat(postId: String? = null) = navigate(route = AiChatRoute(postId))

fun NavGraphBuilder.aiChatScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
) {
    composable<AiChatRoute> {
        AiChatScreen(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
            onSetupTopBar = onSetupTopBar
        )
    }
}