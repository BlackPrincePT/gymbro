package com.pegio.workout.presentation.screen.userworkouts

import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.workout.FetchWorkoutsByIdUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserWorkoutsViewModel @Inject constructor(
    private val fetchWorkoutsByUserId: FetchWorkoutsByIdUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper
) : BaseViewModel<UserWorkoutsUiState, UserWorkoutsUiEffect, UserWorkoutsUiEvent>(initialState = UserWorkoutsUiState()) {



    override fun onEvent(event: UserWorkoutsUiEvent) {
        when (event) {
            is UserWorkoutsUiEvent.FetchWorkouts -> fetchWorkoutsId()
            UserWorkoutsUiEvent.OnBackClick -> sendEffect(UserWorkoutsUiEffect.NavigateBack)
            is UserWorkoutsUiEvent.StartWorkout -> startWorkout(event.workoutId)
        }
    }

    init {
        fetchWorkoutsId()
    }

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


    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}