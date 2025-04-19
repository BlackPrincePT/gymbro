package com.pegio.gymbro.activity.state

import com.pegio.common.presentation.state.TopBarState

sealed interface MainActivityUiEvent {

    // App Bar
    data class OnUpdateTopBarState(val value: TopBarState) : MainActivityUiEvent

    // Drawer
    data object OnOpenDrawer : MainActivityUiEvent
    data object OnAccountClick : MainActivityUiEvent
    data object OnWorkoutPlanClick : MainActivityUiEvent
    data object OnSignOutClick : MainActivityUiEvent
}