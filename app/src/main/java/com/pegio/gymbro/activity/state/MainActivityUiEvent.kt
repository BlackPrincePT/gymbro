package com.pegio.gymbro.activity.state

import com.pegio.gymbro.activity.TopBarState

sealed interface MainActivityUiEvent {

    // App Bar
    data class OnUpdateTopBarState(val value: TopBarState) : MainActivityUiEvent

    // Open-Close Drawer
    data object OnOpenDrawer : MainActivityUiEvent
    data object OnCloseDrawer : MainActivityUiEvent

    // Drawer
    data object OnAccountClick : MainActivityUiEvent
    data object OnWorkoutPlanClick : MainActivityUiEvent
    data object OnSignOutClick : MainActivityUiEvent
}