package com.example.presentation.screen.workout_plan

import com.example.presentation.model.UiWorkoutPlan

data class WorkoutPlanUiState(
    val plans: List<UiWorkoutPlan> = emptyList(),
    val isLoading: Boolean = false,
)
