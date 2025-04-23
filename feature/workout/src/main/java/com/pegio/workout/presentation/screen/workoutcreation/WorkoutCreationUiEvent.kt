package com.pegio.workout.presentation.screen.workoutcreation

import com.pegio.workout.presentation.model.UiWorkout

sealed interface WorkoutCreationUiEvent {
    data class AddWorkout(val workout: UiWorkout) : WorkoutCreationUiEvent
    data class RemoveWorkout(val workoutId: String) : WorkoutCreationUiEvent
    data object OnAddWorkoutClick : WorkoutCreationUiEvent

    data object OnBackClick: WorkoutCreationUiEvent
}