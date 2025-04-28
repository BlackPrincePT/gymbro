package com.pegio.workout.presentation.screen.workoutplan.state

sealed interface WorkoutPlanUiEvent {
    data object LoadInitialPlans : WorkoutPlanUiEvent
    data object OnInfoClick: WorkoutPlanUiEvent
    data class StartWorkout(val workoutId: String) : WorkoutPlanUiEvent

    data object OnBackClick: WorkoutPlanUiEvent
}