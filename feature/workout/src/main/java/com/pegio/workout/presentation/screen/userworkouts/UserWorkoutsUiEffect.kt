package com.pegio.workout.presentation.screen.userworkouts

import androidx.annotation.StringRes

sealed interface UserWorkoutsUiEffect {
    data class Failure(@StringRes val errorRes: Int) : UserWorkoutsUiEffect
    data class NavigateToWorkout(val workoutId: String) : UserWorkoutsUiEffect

    data object NavigateBack: UserWorkoutsUiEffect
}