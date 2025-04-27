package com.pegio.workout.presentation.screen.userworkouts

import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.domain.usecase.common.GetCurrentAuthUserUseCase
import com.pegio.domain.usecase.workout.FetchWorkoutsByUserIdUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserWorkoutsViewModel @Inject constructor(
    private val fetchWorkoutsByUserId: FetchWorkoutsByUserIdUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper,
    getCurrentAuthUser: GetCurrentAuthUserUseCase
) : BaseViewModel<UserWorkoutsUiState, UserWorkoutsUiEffect, UserWorkoutsUiEvent>(initialState = UserWorkoutsUiState()) {

    override fun onEvent(event: UserWorkoutsUiEvent) {
        when (event) {
            is UserWorkoutsUiEvent.FetchWorkouts -> {} //fetchWorkouts()
            UserWorkoutsUiEvent.OnBackClick -> sendEffect(UserWorkoutsUiEffect.NavigateBack)
        }
    }

    init {
        getCurrentAuthUser()?.let { user ->
            updateState { copy(authorId = user.id) }
        }
    }



    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}