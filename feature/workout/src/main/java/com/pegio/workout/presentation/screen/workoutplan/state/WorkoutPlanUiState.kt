package com.pegio.workout.presentation.screen.workoutplan.state

import com.pegio.workout.presentation.model.UiWorkoutPlan

data class WorkoutPlanUiState(
    // Pagination
    val plans: List<UiWorkoutPlan> = emptyList(),
    val endOfPaginationReached: Boolean = false,
    val isRefreshing: Boolean = false,

    val isLoading: Boolean = false,
)
