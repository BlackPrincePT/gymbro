package com.pegio.workout.presentation.screen.workout

import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.domain.usecase.workout.FetchWorkoutsByIdUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val fetchWorkoutsById: FetchWorkoutsByIdUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper,
) : BaseViewModel<WorkoutUiState, WorkoutUiEffect, WorkoutUiEvent>(initialState = WorkoutUiState()) {
    override fun onEvent(event: WorkoutUiEvent) {
        TODO("Not yet implemented")
    }

    override fun setLoading(isLoading: Boolean) {
        TODO("Not yet implemented")
    }


}
