package com.pegio.workout.presentation.screen.userworkouts.state

sealed interface UserWorkoutsUiEvent {
    // Navigation
    data class StartWorkout(val workoutId: String) : UserWorkoutsUiEvent
    data object OnCreateWorkoutClick : UserWorkoutsUiEvent
    data class OnBackClick(val selectedWorkoutId: String? = null) : UserWorkoutsUiEvent
}