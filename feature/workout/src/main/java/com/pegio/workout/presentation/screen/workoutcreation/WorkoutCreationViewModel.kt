package com.pegio.workout.presentation.screen.workoutcreation

import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.workout.presentation.model.UiWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutCreationViewModel @Inject constructor(

): BaseViewModel<WorkoutCreationUiState, WorkoutCreationUiEffect, WorkoutCreationUiEvent>(initialState = WorkoutCreationUiState())  {
    override fun onEvent(event: WorkoutCreationUiEvent) {
        when (event) {
            is WorkoutCreationUiEvent.AddWorkout -> addWorkout(event.workout)
            is WorkoutCreationUiEvent.RemoveWorkout -> removeWorkout(event.workoutId)
            WorkoutCreationUiEvent.OnBackClick -> sendEffect(WorkoutCreationUiEffect.NavigateBack)
            WorkoutCreationUiEvent.OnAddWorkoutClick -> TODO()
        }

    }

    private fun addWorkout(workout: UiWorkout){
        updateState {
            copy(workouts = workouts + workout)
        }
    }

    private fun removeWorkout(workoutId: String){
        updateState {
            copy(workouts = workouts.filter { it.id != workoutId })
        }
    }


    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}