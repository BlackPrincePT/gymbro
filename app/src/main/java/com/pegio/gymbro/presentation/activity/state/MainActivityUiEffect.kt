package com.pegio.gymbro.presentation.activity.state

sealed interface MainActivityUiEffect {

    // Open-Close Drawer
    data object OpenDrawer: MainActivityUiEffect
    data object CloseDrawer: MainActivityUiEffect

    // Drawer
    data object NavigateToAccount: MainActivityUiEffect
    data object NavigateToWorkoutPlan: MainActivityUiEffect
    data object NavigateToAuth: MainActivityUiEffect
}