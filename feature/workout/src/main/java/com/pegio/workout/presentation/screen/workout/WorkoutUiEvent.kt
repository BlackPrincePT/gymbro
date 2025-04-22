package com.pegio.workout.presentation.screen.workout


sealed interface WorkoutUiEvent {
    data class FetchWorkouts(val workoutId: String) : WorkoutUiEvent
    data object OnNextClick : WorkoutUiEvent
    data object OnPreviousClick:  WorkoutUiEvent
    data class OnReadTTSClick(val textToRead: String) : WorkoutUiEvent
    data object OnToggleTTSClick : WorkoutUiEvent

    // Navigation
    data object OnBackClick: WorkoutUiEvent
}