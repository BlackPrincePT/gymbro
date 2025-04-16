package com.pegio.workout.presentation.screen.workout_plan

import com.pegio.workout.presentation.model.UiWorkoutPlan

data class WorkoutPlanUiState(
    val plans: List<UiWorkoutPlan> = emptyList(),
    val isLoading: Boolean = false,
)
