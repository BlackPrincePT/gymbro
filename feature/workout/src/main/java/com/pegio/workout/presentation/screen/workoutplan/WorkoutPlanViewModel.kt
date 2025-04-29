package com.pegio.workout.presentation.screen.workoutplan

import com.pegio.common.core.DataError
import com.pegio.common.core.getOrElse
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.domain.usecase.workout.FetchNextWorkoutPlansPageUseCase
import com.pegio.domain.usecase.workout.RefreshWorkoutPlanPaginationUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutPlanMapper
import com.pegio.workout.presentation.screen.workoutplan.state.WorkoutPlanUiEffect
import com.pegio.workout.presentation.screen.workoutplan.state.WorkoutPlanUiEvent
import com.pegio.workout.presentation.screen.workoutplan.state.WorkoutPlanUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutPlanViewModel @Inject constructor(
    private val fetchNextWorkoutPlansPageUseCase: FetchNextWorkoutPlansPageUseCase,
    private val refreshWorkoutPlanPagination: RefreshWorkoutPlanPaginationUseCase,
    private val uiWorkoutPlanMapper: UiWorkoutPlanMapper
) : BaseViewModel<WorkoutPlanUiState, WorkoutPlanUiEffect, WorkoutPlanUiEvent>(initialState = WorkoutPlanUiState()) {

    init {
        refreshUserWorkouts()
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

            WorkoutPlanUiEvent.LoadMoreUserWorkouts -> loadNextWorkoutPlansPage()
            WorkoutPlanUiEvent.RefreshUserWorkouts -> refreshUserWorkouts()
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun loadNextWorkoutPlansPage() = launchWithLoading {
        if (uiState.endOfPaginationReached) return@launchWithLoading

        val fetchedWorkoutPlans = fetchNextWorkoutPlansPageUseCase()
            .getOrElse { error ->
                if (error == DataError.Pagination.END_OF_PAGINATION_REACHED)
                    updateState { copy(endOfPaginationReached = true) }

                return@launchWithLoading
            }
            .map(uiWorkoutPlanMapper::mapFromDomain)

        updateState { copy(isRefreshing = false, plans = plans.plus(fetchedWorkoutPlans)) }
    }

    private fun refreshUserWorkouts() {
        refreshWorkoutPlanPagination()
        updateState { copy(plans = emptyList(), isRefreshing = true, endOfPaginationReached = false) }
        loadNextWorkoutPlansPage()
    }
}

