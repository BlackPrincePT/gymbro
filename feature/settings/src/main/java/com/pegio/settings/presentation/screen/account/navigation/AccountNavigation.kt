package com.pegio.settings.presentation.screen.account.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.settings.presentation.screen.account.AccountScreen
import kotlinx.serialization.Serializable

@Serializable
data object AccountRoute

fun NavController.navigateToAccount() = navigate(route = AccountRoute)

fun NavGraphBuilder.accountScreen(
    onBackClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit,
) {
    composable<AccountRoute> {
        AccountScreen(
            onBackClick = onBackClick,
            onSetupTopBar = onSetupTopBar
        )
    }
}