package com.pegio.workout.presentation.screen.workout

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.workout.FetchWorkoutsByIdUseCase
import com.pegio.workout.presentation.core.TextToSpeechRepositoryImpl
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import com.pegio.workout.presentation.screen.workout.WorkoutUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val fetchWorkoutsById: FetchWorkoutsByIdUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper,
    private val textToSpeechRepository: TextToSpeechRepositoryImpl,
) : BaseViewModel<WorkoutUiState, WorkoutUiEffect, WorkoutUiEvent>(initialState = WorkoutUiState()) {

    private var timerJob: Job? = null

    override fun onEvent(event: WorkoutUiEvent) {
        when (event) {
            is WorkoutUiEvent.FetchWorkouts -> fetchWorkouts(event.workoutId)
            is WorkoutUiEvent.OnNextClick -> nextWorkout()
            WorkoutUiEvent.OnPreviousClick -> previousWorkout()
            WorkoutUiEvent.OnBackClick -> sendEffect(WorkoutUiEffect.NavigateBack)
            is WorkoutUiEvent.OnReadTTSClick -> readDescription(event.textToRead)
            WorkoutUiEvent.OnToggleTTSClick -> toggleTTSState()
            WorkoutUiEvent.PauseTimer -> pauseTimer()
            WorkoutUiEvent.ResumeTimer -> resumeTimer()
        }
    }

    private fun fetchWorkouts(workoutId: String) {
        launchWithLoading {
            fetchWorkoutsById(workoutId)
                .onSuccess { workouts ->
                    val mappedWorkouts = workouts.map(uiWorkoutMapper::mapFromDomain)
                    updateState {
                        copy(
                            workouts = mappedWorkouts,
                            currentWorkoutIndex = 0,
                            timeRemaining = workouts[0].value
                        )
                    }
                }.onFailure { error ->
                    sendEffect(WorkoutUiEffect.Failure(error.toStringResId()))
                }
        }
    }

    private fun nextWorkout() {
        stopTimer()
        if (uiState.currentWorkoutIndex == uiState.workouts.lastIndex) {
            textToSpeechRepository.shutdown()
            sendEffect(WorkoutUiEffect.NavigateBack)
        } else if (uiState.workouts.size > uiState.currentWorkoutIndex + 1) {
            val newIndex = uiState.currentWorkoutIndex + 1
            updateState {
                copy(
                    currentWorkoutIndex = newIndex,
                    timeRemaining = workouts[newIndex].value
                )
            }
        }
    }


    private fun previousWorkout() {
        stopTimer()
        if (uiState.currentWorkoutIndex > 0) {
            updateState {
                copy(
                    currentWorkoutIndex = currentWorkoutIndex - 1,
                    timeRemaining = workouts[currentWorkoutIndex - 1].value
                )
            }
        }
    }

    private fun readDescription(textToRead: String) {
        if (uiState.isTTSActive) {
            textToSpeechRepository.speak(textToRead)
            updateState { copy(isTTSActive = true) }
        }
    }

    private fun toggleTTSState() {
        if (uiState.isTTSActive) {
            textToSpeechRepository.stop()
            updateState { copy(isTTSActive = false) }
        } else {
            updateState { copy(isTTSActive = true) }

        }
    }

    private fun pauseTimer() {
        cancelExistingTimer()
        updateState { copy(timerState = TimerState.PAUSED) }
    }

    private fun resumeTimer() {
        cancelExistingTimer()
        val currentTimeRemaining = uiState.timeRemaining

        updateState { copy(timerState = TimerState.RUNNING) }

        startCountdownTimer(currentTimeRemaining)
    }


    private fun stopTimer() {
        cancelExistingTimer()
        updateState { copy(timeRemaining = 0, timerState = TimerState.PAUSED) }
    }

    private fun startCountdownTimer(startTimeInSeconds: Int) {
        timerJob = viewModelScope.launch {
            for (second in startTimeInSeconds downTo 0) {
                updateState { copy(timeRemaining = second) }
                delay(1000L)
            }

            updateState { copy(timerState = TimerState.PAUSED) }
            nextWorkout()
        }
    }

    private fun cancelExistingTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        textToSpeechRepository.shutdown()
    }

    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }

}
