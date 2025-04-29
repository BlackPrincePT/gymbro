package com.pegio.workout.presentation.screen.userworkouts

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pegio.common.core.DataError
import com.pegio.common.core.getOrElse
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.domain.usecase.workout.FetchNextUserWorkoutsPageUseCase
import com.pegio.domain.usecase.workout.RefreshWorkoutsPaginationUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import com.pegio.workout.presentation.screen.userworkouts.navigation.UserWorkoutsRoute
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiEffect
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiEvent
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserWorkoutsViewModel @Inject constructor(
    private val fetchNextUserWorkoutsPage: FetchNextUserWorkoutsPageUseCase,
    private val resetWorkoutsPagination: RefreshWorkoutsPaginationUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<UserWorkoutsUiState, UserWorkoutsUiEffect, UserWorkoutsUiEvent>(initialState = UserWorkoutsUiState()) {

    private val args = savedStateHandle.toRoute<UserWorkoutsRoute>()

    init {
        refreshUserWorkouts()
        updateState { copy(isChoosing = args.isChoosing) }
    }

    override fun onEvent(event: UserWorkoutsUiEvent) {
        when (event) {

            // Pagination
            UserWorkoutsUiEvent.LoadMoreUserWorkouts -> loadNextUserWorkoutsPage()
            UserWorkoutsUiEvent.RefreshUserWorkouts -> refreshUserWorkouts()

            // Navigation
            is UserWorkoutsUiEvent.OnBackClick -> sendEffect(UserWorkoutsUiEffect.NavigateBack(event.selectedWorkoutId))
            is UserWorkoutsUiEvent.StartWorkout -> sendEffect(UserWorkoutsUiEffect.NavigateToWorkout(event.workoutId))
            UserWorkoutsUiEvent.OnCreateWorkoutClick -> sendEffect(UserWorkoutsUiEffect.NavigateToWorkoutCreation)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun loadNextUserWorkoutsPage() = launchWithLoading {
        if (uiState.endOfPaginationReached) return@launchWithLoading

        val fetchedWorkouts = fetchNextUserWorkoutsPage()
            .getOrElse { error ->
                if (error == DataError.Pagination.END_OF_PAGINATION_REACHED)
                    updateState { copy(endOfPaginationReached = true) }

                return@launchWithLoading
            }
            .map(uiWorkoutMapper::mapFromDomain)

        updateState { copy(isRefreshing = false, workouts = workouts.plus(fetchedWorkouts)) }
    }

    private fun refreshUserWorkouts() {
        resetWorkoutsPagination()
        updateState { copy(workouts = emptyList(), isRefreshing = true, endOfPaginationReached = false) }
        loadNextUserWorkoutsPage()
    }
}