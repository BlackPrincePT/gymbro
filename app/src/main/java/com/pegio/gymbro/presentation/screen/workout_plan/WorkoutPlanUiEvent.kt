package com.pegio.gymbro.presentation.screen.workout_plan

sealed interface WorkoutPlanUiEvent {
    data object LoadInitialPlans : WorkoutPlanUiEvent
    data object OnInfoClick: WorkoutPlanUiEvent

    data object OnBackClick: WorkoutPlanUiEvent
}