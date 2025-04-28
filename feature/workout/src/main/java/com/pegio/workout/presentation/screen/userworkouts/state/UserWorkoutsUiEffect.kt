package com.pegio.workout.presentation.screen.userworkouts.state

import androidx.annotation.StringRes

sealed interface UserWorkoutsUiEffect {
    data class Failure(@StringRes val errorRes: Int) : UserWorkoutsUiEffect

    // Navigation
    data class NavigateToWorkout(val workoutId: String) : UserWorkoutsUiEffect
    data object NavigateToWorkoutCreation : UserWorkoutsUiEffect
    data class NavigateBack(val selectedWorkoutId: String? = null) : UserWorkoutsUiEffect
}