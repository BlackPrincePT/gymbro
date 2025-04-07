package com.pegio.gymbro.presentation.screen.workout_plan

sealed interface WorkoutPlanUiEvent {
    data object LoadInitialPlans : WorkoutPlanUiEvent
}