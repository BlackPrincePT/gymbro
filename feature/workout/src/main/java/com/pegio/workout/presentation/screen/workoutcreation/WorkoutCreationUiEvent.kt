package com.pegio.workout.presentation.screen.workoutcreation

import com.pegio.workout.presentation.model.UiWorkout

sealed interface WorkoutCreationUiEvent {
    data class RemoveWorkout(val workoutId: String) : WorkoutCreationUiEvent
    data class OnEditWorkout(val workout: UiWorkout): WorkoutCreationUiEvent

    data object OnAddWorkoutClick : WorkoutCreationUiEvent
    data object OnDismissDialog : WorkoutCreationUiEvent
    data object OnSaveWorkout : WorkoutCreationUiEvent

    data object OnBackClick: WorkoutCreationUiEvent
}