package com.pegio.workout.presentation.screen.userworkouts

import com.pegio.workout.presentation.model.UiWorkout

data class UserWorkoutsUiState(
    val isLoading: Boolean = false,
    val workouts: List<UiWorkout> = emptyList(),
    val authorId: String = ""
)
