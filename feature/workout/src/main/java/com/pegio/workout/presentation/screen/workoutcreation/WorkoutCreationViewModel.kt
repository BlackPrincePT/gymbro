package com.pegio.workout.presentation.screen.workoutcreation

import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.model.Workout
import com.pegio.workout.presentation.model.UiWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutCreationViewModel @Inject constructor(

) : BaseViewModel<WorkoutCreationUiState, WorkoutCreationUiEffect, WorkoutCreationUiEvent>(
    initialState = WorkoutCreationUiState()
) {
    override fun onEvent(event: WorkoutCreationUiEvent) {
        when (event) {
            is WorkoutCreationUiEvent.RemoveWorkout -> removeWorkout(event.workoutId)
            WorkoutCreationUiEvent.OnBackClick -> sendEffect(WorkoutCreationUiEffect.NavigateBack)
            WorkoutCreationUiEvent.OnAddWorkoutClick -> showAddWorkoutDialog()
            WorkoutCreationUiEvent.OnDismissDialog -> hideAddWorkoutDialog()
            WorkoutCreationUiEvent.OnSaveWorkout -> saveWorkout()
            is WorkoutCreationUiEvent.OnEditWorkout -> editWorkout(event.workout)
        }
    }

    private fun showAddWorkoutDialog() {
        updateState {
            copy(
                showAddWorkoutDialog = true,
                newWorkout = UiWorkout(
                    name = "",
                    description = "",
                    workoutType = Workout.WorkoutType.TIMED,
                    value = 0,
                    sets = 0,
                    muscleGroups = emptyList(),
                    workoutImage = ""
                )
            )
        }
    }

    private fun editWorkout(workout: UiWorkout) {
        updateState {
            copy(
                newWorkout = workout,
                showAddWorkoutDialog = true
            )
        }
    }


    private fun hideAddWorkoutDialog() {
        updateState {
            copy(
                showAddWorkoutDialog = false,
            )
        }
    }

    private fun saveWorkout() {
        val workout = uiState.newWorkout

        updateState {
            val updatedList = workouts.toMutableList()
            val index = updatedList.indexOfFirst { it.id == workout.id }

            if (index >= 0) {
                updatedList[index] = workout
            } else {
                updatedList.add(workout)
            }

            copy(
                workouts = updatedList,
                showAddWorkoutDialog = false,
            )
        }
    }

    private fun removeWorkout(workoutId: String) {
        updateState {
            copy(workouts = workouts.filter { it.id != workoutId })
        }
    }

    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}