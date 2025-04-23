package com.pegio.workout.presentation.screen.workout

import androidx.annotation.StringRes

sealed interface WorkoutUiEffect {
    data class Failure(@StringRes val errorRes: Int) : WorkoutUiEffect

    // Navigation
    data object NavigateBack: WorkoutUiEffect
}