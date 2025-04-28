package com.pegio.workout.presentation.screen.workoutcreation.state


import com.pegio.workout.presentation.model.UiExercise

sealed interface WorkoutCreationUiEvent {
    // Workout actions
    data object OnAddWorkoutClick : WorkoutCreationUiEvent
    data object OnUploadWorkouts : WorkoutCreationUiEvent
    data class RemoveWorkout(val workoutId: String) : WorkoutCreationUiEvent
    data class OnEditWorkout(val workout: UiExercise) : WorkoutCreationUiEvent

    //Workout Dialog
    data object OnDismissDialog : WorkoutCreationUiEvent
    data object OnSaveWorkout : WorkoutCreationUiEvent

    // Fields
    data class OnTitleChange(val title: String) : WorkoutCreationUiEvent
    data class OnDescriptionChange(val description: String) : WorkoutCreationUiEvent

    // Navigation
    data object OnBackClick : WorkoutCreationUiEvent
}