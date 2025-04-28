package com.pegio.gymbro.activity.state

import android.content.Context
import com.pegio.common.presentation.state.TopBarState

sealed interface MainActivityUiEvent {

    // App Bar
    data class OnUpdateTopBarState(val value: TopBarState) : MainActivityUiEvent

    // Auth
    data class LinkAnonymousAccount(val context: Context) : MainActivityUiEvent
    data object OnSignOutClick : MainActivityUiEvent

    // Drawer
    data object OnOpenDrawerClick : MainActivityUiEvent

    // Navigation
    data object OnProfileClick : MainActivityUiEvent
    data object OnAccountClick : MainActivityUiEvent
    data object OnSettingsClick : MainActivityUiEvent
    data object OnWorkoutPlanClick : MainActivityUiEvent
    data object OnUserWorkoutsClick : MainActivityUiEvent
}