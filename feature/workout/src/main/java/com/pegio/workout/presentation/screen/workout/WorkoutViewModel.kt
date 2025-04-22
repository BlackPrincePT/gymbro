package com.pegio.workout.presentation.screen.workout

import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.texttospeech.ShutdownTextToSpeechUseCase
import com.pegio.domain.usecase.texttospeech.SpeakTextUseCase
import com.pegio.domain.usecase.texttospeech.StopSpeakingUseCase
import com.pegio.domain.usecase.workout.FetchWorkoutsByIdUseCase
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val fetchWorkoutsById: FetchWorkoutsByIdUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper,
    private val speakText: SpeakTextUseCase,
    private val stopSpeaking: StopSpeakingUseCase,
    private val shutdownTextToSpeech: ShutdownTextToSpeechUseCase
) : BaseViewModel<WorkoutUiState, WorkoutUiEffect, WorkoutUiEvent>(initialState = WorkoutUiState()) {


    override fun onEvent(event: WorkoutUiEvent) {
        when (event) {
            is WorkoutUiEvent.FetchWorkouts -> fetchWorkout(event.workoutId)
            is WorkoutUiEvent.OnNextClick -> nextWorkout()
            WorkoutUiEvent.OnPreviousClick -> previousWorkout()
            WorkoutUiEvent.OnBackClick -> sendEffect(WorkoutUiEffect.NavigateBack)
            is WorkoutUiEvent.OnReadTTSClick -> readDescription(event.textToRead)
            WorkoutUiEvent.OnToggleTTSClick -> toggleTTSState()
        }
    }

    private fun fetchWorkout(workoutId: String) {
        launchWithLoading {
            fetchWorkoutsById(workoutId)
                .onSuccess { workouts ->
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
        if (uiState.currentWorkoutIndex == uiState.workouts.lastIndex) {
            sendEffect(WorkoutUiEffect.NavigateBack)
        } else if (uiState.workouts.size > uiState.currentWorkoutIndex + 1) {
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

    private fun readDescription(textToRead: String) {
        if (uiState.isTTSActive) {
            speakText(textToRead)
            updateState { copy(isTTSActive = true) }
        }
    }

    private fun toggleTTSState() {
        if (uiState.isTTSActive) {
            stopSpeaking
            shutdownTextToSpeech()
            updateState { copy(isTTSActive = false) }
        } else {
            updateState { copy(isTTSActive = true) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        shutdownTextToSpeech()
    }

    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }

}
