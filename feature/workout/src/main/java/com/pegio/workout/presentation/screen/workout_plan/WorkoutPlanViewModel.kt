package com.pegio.workout.presentation.screen.workout_plan

import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.workout.ObserveWorkoutPlansPagingStreamUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutPlanMapper
import com.pegio.workout.presentation.screen.workout_plan.state.WorkoutPlanUiEffect
import com.pegio.workout.presentation.screen.workout_plan.state.WorkoutPlanUiEvent
import com.pegio.workout.presentation.screen.workout_plan.state.WorkoutPlanUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class WorkoutPlanViewModel @Inject constructor(
    private val observeWorkoutPlansPagingStreamUseCase: ObserveWorkoutPlansPagingStreamUseCase,
    private val uiWorkoutPlanMapper: UiWorkoutPlanMapper
) : BaseViewModel<WorkoutPlanUiState, WorkoutPlanUiEffect, WorkoutPlanUiEvent>(initialState = WorkoutPlanUiState()) {

    init {
        loadWorkoutPlans()
    }

    override fun onEvent(event: WorkoutPlanUiEvent) {
        when (event) {
            WorkoutPlanUiEvent.LoadInitialPlans -> loadWorkoutPlans()
            WorkoutPlanUiEvent.OnBackClick -> sendEffect(WorkoutPlanUiEffect.NavigateBack)
            WorkoutPlanUiEvent.OnInfoClick -> sendEffect(WorkoutPlanUiEffect.NavigateToAiChat)
            is WorkoutPlanUiEvent.StartWorkout -> sendEffect(WorkoutPlanUiEffect.NavigateToWorkout(event.workoutId))
        }
    }

    private fun loadWorkoutPlans() = launchWithLoading {
        observeWorkoutPlansPagingStreamUseCase()
            .onSuccess { plans ->
                val mappedPlans = plans.map(uiWorkoutPlanMapper::mapFromDomain)
                updateState { copy(plans = mappedPlans) }
            }
            .onFailure { error ->
                sendEffect(WorkoutPlanUiEffect.Failure(error.toStringResId()))
            }
            .launchIn(viewModelScope)
    }


    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}

