package com.pegio.workout.presentation.screen.userworkouts

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.workout.FetchUserWorkoutsUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import com.pegio.workout.presentation.screen.userworkouts.navigation.UserWorkoutsRoute
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiEffect
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiEvent
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserWorkoutsViewModel @Inject constructor(
    private val fetchWorkoutsByUserId: FetchUserWorkoutsUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<UserWorkoutsUiState, UserWorkoutsUiEffect, UserWorkoutsUiEvent>(initialState = UserWorkoutsUiState()) {

    private val args = savedStateHandle.toRoute<UserWorkoutsRoute>()

    init {
        fetchWorkoutsId()
        updateState { copy(isChoosing = args.isChoosing) }
    }

    override fun onEvent(event: UserWorkoutsUiEvent) {
        when (event) {
            //Navigation
            is UserWorkoutsUiEvent.OnBackClick -> sendEffect(UserWorkoutsUiEffect.NavigateBack(event.selectedWorkoutId))
            is UserWorkoutsUiEvent.StartWorkout -> startWorkout(event.workoutId)
            UserWorkoutsUiEvent.OnCreateWorkoutClick -> sendEffect(UserWorkoutsUiEffect.NavigateToWorkoutCreation)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun fetchWorkoutsId() {
        launchWithLoading {
            fetchWorkoutsByUserId().onSuccess { workouts ->

                val mappedWorkouts = workouts.map(uiWorkoutMapper::mapFromDomain)

                updateState {
                    copy(
                        workouts = mappedWorkouts
                    )
                }
            }.onFailure { error ->
                sendEffect(UserWorkoutsUiEffect.Failure(error.toStringResId()))
            }
        }
    }

    private fun startWorkout(workoutId: String) {
        sendEffect(UserWorkoutsUiEffect.NavigateToWorkout(workoutId))
    }
}