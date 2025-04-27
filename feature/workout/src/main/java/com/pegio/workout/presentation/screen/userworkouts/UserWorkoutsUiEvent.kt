package com.pegio.workout.presentation.screen.userworkouts

sealed interface UserWorkoutsUiEvent {
    data object FetchWorkouts : UserWorkoutsUiEvent

    data object OnBackClick: UserWorkoutsUiEvent
}