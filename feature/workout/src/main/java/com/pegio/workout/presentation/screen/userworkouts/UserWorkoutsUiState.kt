package com.pegio.workout.presentation.screen.userworkouts

import com.pegio.workout.presentation.model.UiExercise

data class UserWorkoutsUiState(
    val isLoading: Boolean = false,
    val workouts: List<UiExercise> = emptyList(),
    val authorId: String = ""
)
