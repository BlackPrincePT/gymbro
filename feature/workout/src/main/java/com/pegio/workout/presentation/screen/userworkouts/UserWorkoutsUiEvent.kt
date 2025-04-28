package com.pegio.workout.presentation.screen.userworkouts

sealed interface UserWorkoutsUiEvent {
    data class StartWorkout(val workoutId: String) : UserWorkoutsUiEvent
    data object FetchWorkouts : UserWorkoutsUiEvent

    data object OnCreateWorkoutClick : UserWorkoutsUiEvent
    data object OnBackClick: UserWorkoutsUiEvent
}