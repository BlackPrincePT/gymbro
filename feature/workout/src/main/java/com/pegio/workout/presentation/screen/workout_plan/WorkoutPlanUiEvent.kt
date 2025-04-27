package com.pegio.workout.presentation.screen.workout_plan

sealed interface WorkoutPlanUiEvent {
    data object LoadInitialPlans : WorkoutPlanUiEvent
    data object OnInfoClick: WorkoutPlanUiEvent
    data class StartWorkout(val workoutId: String) : WorkoutPlanUiEvent

    data object OnBackClick: WorkoutPlanUiEvent
}