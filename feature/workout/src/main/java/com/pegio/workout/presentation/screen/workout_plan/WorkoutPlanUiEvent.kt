package com.pegio.workout.presentation.screen.workout_plan

sealed interface WorkoutPlanUiEvent {
    data object LoadInitialPlans : WorkoutPlanUiEvent
    data object OnInfoClick: WorkoutPlanUiEvent
    data object OnBackClick: WorkoutPlanUiEvent
    data class StartWorkout(val difficulty: String) : WorkoutPlanUiEvent
}