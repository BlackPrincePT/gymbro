package com.pegio.gymbro.activity.state

import androidx.annotation.StringRes

sealed interface MainActivityUiEffect {

    // Open-Close Drawer
    data object OpenDrawer : MainActivityUiEffect
    data object CloseDrawer : MainActivityUiEffect

    // Failure
    data class ShowSnackbar(@StringRes val errorRes: Int) : MainActivityUiEffect

    // Navigation
    data class NavigateToProfile(val userId: String) : MainActivityUiEffect
    data object NavigateToAccount : MainActivityUiEffect
    data object NavigateToSettings : MainActivityUiEffect
    data object NavigateToWorkoutPlan : MainActivityUiEffect
    data object NavigateToAuth : MainActivityUiEffect
    data object NavigateToRegister : MainActivityUiEffect
    data object NavigateToUserWorkouts : MainActivityUiEffect
}