package com.pegio.workout.presentation.screen.workoutplan.state

sealed interface WorkoutPlanUiEvent {
    // Navigation
    data object OnBackClick: WorkoutPlanUiEvent
    data class StartWorkout(val workoutId: String) : WorkoutPlanUiEvent
}