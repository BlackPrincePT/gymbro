package com.pegio.workout.presentation.screen.workoutcreation

import com.pegio.workout.presentation.model.UiExercise

sealed interface WorkoutCreationUiEvent {
    data class RemoveWorkout(val workoutId: String) : WorkoutCreationUiEvent
    data class OnEditWorkout(val workout: UiExercise): WorkoutCreationUiEvent

    data object OnAddWorkoutClick : WorkoutCreationUiEvent
    data object OnDismissDialog : WorkoutCreationUiEvent
    data object OnSaveWorkout : WorkoutCreationUiEvent
    data object OnUploadWorkouts : WorkoutCreationUiEvent

    data class OnTitleChange(val title: String) : WorkoutCreationUiEvent
    data class OnDescriptionChange(val description: String) : WorkoutCreationUiEvent

    data object OnBackClick: WorkoutCreationUiEvent
}