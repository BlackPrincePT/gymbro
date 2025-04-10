package com.pegio.gymbro.presentation.screen.workout_plan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.onFailure
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.usecase.workout_plan.ObserveWorkoutPlansPagingStreamUseCase
import com.pegio.gymbro.presentation.model.mapper.UiWorkoutPlanMapper
import com.pegio.gymbro.presentation.screen.ai_chat.AiChatUiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutPlanViewModel @Inject constructor(
    private val observeWorkoutPlansPagingStreamUseCase: ObserveWorkoutPlansPagingStreamUseCase,
    private val uiWorkoutPlanMapper: UiWorkoutPlanMapper
) : ViewModel() {
    var uiState by mutableStateOf(WorkoutPlanUiState())
        private set

    private val _uiEffect = MutableSharedFlow<WorkoutPlanUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()


    fun onEvent(event: WorkoutPlanUiEvent) {
        when (event) {
            is WorkoutPlanUiEvent.LoadInitialPlans -> {
                loadWorkoutPlans()
            }

            WorkoutPlanUiEvent.OnBackClick -> sendEffect(WorkoutPlanUiEffect.NavigateBack)
            WorkoutPlanUiEvent.OnInfoClick -> sendEffect(WorkoutPlanUiEffect.NavigateToAiChat)
        }
    }

    private fun loadWorkoutPlans() {
        updateState { copy(isLoading = true) }
        observeWorkoutPlansPagingStreamUseCase().onSuccess { plans ->
            val mappedPlans = plans.map(uiWorkoutPlanMapper::mapFromDomain)
            updateState {
                copy(
                    plans = mappedPlans,
                    isLoading = false,
                )
            }
        }.onFailure { error ->
            sendEffect(WorkoutPlanUiEffect.Failure(error = error))
        }.launchIn(viewModelScope)
    }


    private fun updateState(change: WorkoutPlanUiState.() -> WorkoutPlanUiState) {
        uiState = uiState.change()
    }

    private fun sendEffect(effect: WorkoutPlanUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}

