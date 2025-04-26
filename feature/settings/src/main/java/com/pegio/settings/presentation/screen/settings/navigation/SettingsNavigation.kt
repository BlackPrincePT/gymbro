package com.pegio.settings.presentation.screen.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.settings.presentation.screen.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute

fun NavController.navigateToSettings() = navigate(SettingsRoute)

fun NavGraphBuilder.settingsScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {
    composable<SettingsRoute> {
        SettingsScreen(
            onBackClick = onBackClick,
            onSetupTopBar = onSetupTopBar
        )
    }
}