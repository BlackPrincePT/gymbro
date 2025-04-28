package com.pegio.workout.presentation.screen.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.workout.FetchExerciseByIdUseCase
import com.pegio.workout.presentation.core.TextToSpeechManagerImpl
import com.pegio.workout.presentation.model.mapper.UiExerciseMapper
import com.pegio.workout.presentation.screen.workout.navigation.WorkoutRoute
import com.pegio.workout.presentation.screen.workout.state.WorkoutUiEffect
import com.pegio.workout.presentation.screen.workout.state.WorkoutUiEvent
import com.pegio.workout.presentation.screen.workout.state.WorkoutUiState
import com.pegio.workout.presentation.screen.workout.state.WorkoutUiState.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val fetchExerciseByIdUseCase: FetchExerciseByIdUseCase,
    private val uiExerciseMapper: UiExerciseMapper,
    private val textToSpeechRepository: TextToSpeechManagerImpl,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<WorkoutUiState, WorkoutUiEffect, WorkoutUiEvent>(initialState = WorkoutUiState()) {

    private var timerJob: Job? = null
    private val workoutId = savedStateHandle.toRoute<WorkoutRoute>().workoutId

    init {
        fetchWorkouts(workoutId)
    }

    override fun onEvent(event: WorkoutUiEvent) {
        when (event) {
            // Main buttons
            is WorkoutUiEvent.OnNextClick -> nextWorkout()
            WorkoutUiEvent.OnPreviousClick -> previousWorkout()

            // Navigation
            WorkoutUiEvent.OnBackClick -> sendEffect(WorkoutUiEffect.NavigateBack)

            // TTS
            is WorkoutUiEvent.OnReadTTSClick -> readDescription(event.textToRead)
            WorkoutUiEvent.OnToggleTTSClick -> toggleTTSState()

            // Timer
            WorkoutUiEvent.PauseTimer -> pauseTimer()
            WorkoutUiEvent.ResumeTimer -> resumeTimer()
            WorkoutUiEvent.ResetTimer -> resetTimer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        textToSpeechRepository.shutdown()
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun fetchWorkouts(workoutId: String) {
        launchWithLoading {
            fetchExerciseByIdUseCase(workoutId)
                .onSuccess { exercises ->

                    val mappedExercises = exercises.map(uiExerciseMapper::mapFromDomain)

                    updateState {
                        copy(
                            workouts = mappedExercises,
                            currentWorkoutIndex = 0,
                            timeRemaining = mappedExercises[0].value
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
    private fun resetTimer(){
        cancelExistingTimer()
        updateState { copy(timeRemaining = workouts[currentWorkoutIndex].value, timerState = TimerState.NOT_STARTED) }
    }


    private fun stopTimer() {
        cancelExistingTimer()
        updateState { copy(timeRemaining = 0, timerState = TimerState.NOT_STARTED) }
    }

    private fun startCountdownTimer(startTimeInSeconds: Int) {
        timerJob = viewModelScope.launch {
            for (second in startTimeInSeconds downTo 0) {
                updateState { copy(timeRemaining = second) }
                delay(1000L)
            }

            updateState { copy(timerState = TimerState.PAUSED) }
        }
    }

    private fun cancelExistingTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}
