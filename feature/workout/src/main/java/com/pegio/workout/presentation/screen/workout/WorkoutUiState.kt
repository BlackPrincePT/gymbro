package com.pegio.workout.presentation.screen.workout

import com.pegio.workout.presentation.model.UiWorkout

data class WorkoutUiState(
    val workouts: List<UiWorkout> = emptyList(),
    val currentWorkoutIndex: Int = 0,
    val isLoading: Boolean = false,
)