package com.pegio.workout.presentation.screen.userworkouts.state

import com.pegio.workout.presentation.model.UiWorkout

data class UserWorkoutsUiState(
    val isLoading: Boolean = false,
    val isChoosing: Boolean = false,
    val workouts: List<UiWorkout> = emptyList(),
)
