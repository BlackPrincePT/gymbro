package com.pegio.workout.presentation.screen.workoutcreation

import androidx.annotation.StringRes
import com.pegio.workout.presentation.model.UiExercise

data class WorkoutCreationUiState(
    val exercises: List<UiExercise> = emptyList(),
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val showAddWorkoutDialog: Boolean = false,
    val validationError: WorkoutValidationError = WorkoutValidationError(),
    val newWorkout: UiExercise = UiExercise.EMPTY
)

data class WorkoutValidationError(
    @StringRes val workoutImage: Int? = null,
    @StringRes val name: Int? = null,
    @StringRes val description: Int? = null,
    @StringRes val value: Int? = null,
    @StringRes val sets: Int? = null,
    @StringRes val muscleGroups: Int? = null,
    @StringRes val workouts: Int? = null
)