package com.pegio.workout.presentation.screen.userworkouts.state

sealed interface UserWorkoutsUiEvent {
    // Navigation
    data class StartWorkout(val workoutId: String) : UserWorkoutsUiEvent
    data object OnCreateWorkoutClick : UserWorkoutsUiEvent
    data object OnBackClick: UserWorkoutsUiEvent
}