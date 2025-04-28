package com.pegio.workout.presentation.screen.workoutplan.state

import androidx.annotation.StringRes

sealed interface WorkoutPlanUiEffect {
    data class Failure(@StringRes val errorRes: Int) : WorkoutPlanUiEffect

    // Navigation
    data class NavigateToWorkout(val workoutId: String) : WorkoutPlanUiEffect
    data object NavigateBack: WorkoutPlanUiEffect
}