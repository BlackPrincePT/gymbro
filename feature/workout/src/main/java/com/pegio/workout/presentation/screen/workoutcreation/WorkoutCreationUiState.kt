package com.pegio.workout.presentation.screen.workoutcreation

import com.pegio.workout.presentation.model.UiWorkout

data class WorkoutCreationUiState(
    val workouts: List<UiWorkout> = emptyList(),
    val isLoading: Boolean = false
)