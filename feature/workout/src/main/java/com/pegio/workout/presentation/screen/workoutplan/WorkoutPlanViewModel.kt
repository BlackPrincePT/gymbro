package com.pegio.workout.presentation.screen.workoutplan

import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.workout.FetchWorkoutPlansUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutPlanMapper
import com.pegio.workout.presentation.screen.workoutplan.state.WorkoutPlanUiEffect
import com.pegio.workout.presentation.screen.workoutplan.state.WorkoutPlanUiEvent
import com.pegio.workout.presentation.screen.workoutplan.state.WorkoutPlanUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutPlanViewModel @Inject constructor(
    private val fetchWorkoutPlansUseCase: FetchWorkoutPlansUseCase,
    private val uiWorkoutPlanMapper: UiWorkoutPlanMapper
) : BaseViewModel<WorkoutPlanUiState, WorkoutPlanUiEffect, WorkoutPlanUiEvent>(initialState = WorkoutPlanUiState()) {

    init {
        loadWorkoutPlans()
    }

    override fun onEvent(event: WorkoutPlanUiEvent) {
        when (event) {
            // Navigation
            WorkoutPlanUiEvent.OnBackClick -> sendEffect(WorkoutPlanUiEffect.NavigateBack)
            is WorkoutPlanUiEvent.StartWorkout -> sendEffect(
                WorkoutPlanUiEffect.NavigateToWorkout(
                    event.workoutId
                )
            )
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun loadWorkoutPlans() = launchWithLoading {
        fetchWorkoutPlansUseCase()

            .onSuccess { plans ->
                val mappedPlans = plans.map(uiWorkoutPlanMapper::mapFromDomain)
                updateState { copy(plans = mappedPlans) }
            }
            .onFailure { error ->
                sendEffect(WorkoutPlanUiEffect.Failure(error.toStringResId()))
            }
    }
}

