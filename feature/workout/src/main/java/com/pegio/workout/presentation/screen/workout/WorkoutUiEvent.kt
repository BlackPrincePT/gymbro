package com.pegio.workout.presentation.screen.workout


sealed interface WorkoutUiEvent {
    data class FetchWorkouts(val workoutId: String) : WorkoutUiEvent
    data object OnNextClick : WorkoutUiEvent
    data object OnPreviousClick:  WorkoutUiEvent

    // Navigation
    data object OnBackClick: WorkoutUiEvent
}