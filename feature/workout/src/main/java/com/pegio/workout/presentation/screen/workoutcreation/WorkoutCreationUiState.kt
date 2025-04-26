package com.pegio.workout.presentation.screen.workoutcreation

import androidx.annotation.StringRes
import com.pegio.workout.presentation.model.UiWorkout

data class WorkoutCreationUiState(
    val workouts: List<UiWorkout> = emptyList(),
    val isLoading: Boolean = false,
    val showAddWorkoutDialog: Boolean = false,
    val validationError: WorkoutValidationError = WorkoutValidationError(),
    val newWorkout: UiWorkout = UiWorkout.EMPTY
)

data class WorkoutValidationError(
    @StringRes val workoutImage: Int? = null,
    @StringRes val name: Int? = null,
    @StringRes val description: Int? = null,
    @StringRes val value: Int? = null,
    @StringRes val sets: Int? = null,
    @StringRes val workoutType : Int? = null,
    @StringRes val muscleGroups: Int? = null,
)