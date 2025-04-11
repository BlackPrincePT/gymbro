package com.pegio.gymbro.presentation.screen.workout_plan

import com.pegio.gymbro.presentation.model.UiWorkoutPlan

data class WorkoutPlanUiState(
    val plans: List<UiWorkoutPlan> = emptyList(),
    val isLoading: Boolean = false,
)
