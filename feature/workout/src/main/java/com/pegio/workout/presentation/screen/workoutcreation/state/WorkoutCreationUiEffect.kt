package com.pegio.workout.presentation.screen.workoutcreation.state

import androidx.annotation.StringRes

sealed interface WorkoutCreationUiEffect {
    data class Failure(@StringRes val errorRes: Int) : WorkoutCreationUiEffect

    data object NavigateBack: WorkoutCreationUiEffect
}