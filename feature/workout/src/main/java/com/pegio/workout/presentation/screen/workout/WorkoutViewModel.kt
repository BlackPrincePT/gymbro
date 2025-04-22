package com.pegio.workout.presentation.screen.workout

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.workout.FetchWorkoutsByIdUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val fetchWorkoutsById: FetchWorkoutsByIdUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper,
) : BaseViewModel<WorkoutUiState, WorkoutUiEffect, WorkoutUiEvent>(initialState = WorkoutUiState()) {


    override fun onEvent(event: WorkoutUiEvent) {
        when (event) {
            is WorkoutUiEvent.FetchWorkouts -> fetchWorkout(event.workoutId)
            is WorkoutUiEvent.OnNextClick -> nextWorkout()
            WorkoutUiEvent.OnPreviousClick ->previousWorkout()
            WorkoutUiEvent.OnBackClick -> sendEffect(WorkoutUiEffect.NavigateBack)
        }
    }

    private fun fetchWorkout(workoutId: String) {
        launchWithLoading {
            fetchWorkoutsById(workoutId).onSuccess { workouts ->
                val mappedWorkouts = workouts.map(uiWorkoutMapper::mapFromDomain)
                updateState {
                    copy(
                        workouts = mappedWorkouts,
                        currentWorkoutIndex = 0,
                    )
                }
            }.onFailure { error ->
                sendEffect(WorkoutUiEffect.Failure(error.toStringResId()))
            }
        }
    }

    private fun nextWorkout() {
        if (uiState.workouts.size > uiState.currentWorkoutIndex + 1) {
            val newIndex = uiState.currentWorkoutIndex + 1
            updateState {
                copy(currentWorkoutIndex = newIndex)
            }
        }
    }

    private fun previousWorkout() {
        if (uiState.currentWorkoutIndex > 0) {
            updateState {
                copy(currentWorkoutIndex = currentWorkoutIndex - 1)
            }
        }
    }

    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}
