package com.pegio.workout.presentation.screen.userworkouts.state

import com.pegio.workout.presentation.model.UiWorkout

data class UserWorkoutsUiState(

    val isLoading: Boolean = false,
    val isChoosing: Boolean = false,

    // Pagination
    val workouts: List<UiWorkout> = emptyList(),
    val endOfPaginationReached: Boolean = false,
    val isRefreshing: Boolean = false,
)
