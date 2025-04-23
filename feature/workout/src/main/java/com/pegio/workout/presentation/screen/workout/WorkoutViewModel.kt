package com.pegio.workout.presentation.screen.workout

import androidx.lifecycle.viewModelScope
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.texttospeech.ShutdownTextToSpeechUseCase
import com.pegio.domain.usecase.texttospeech.SpeakTextUseCase
import com.pegio.domain.usecase.texttospeech.StopSpeakingUseCase
import com.pegio.domain.usecase.workout.FetchWorkoutsByIdUseCase
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
    private val speakText: SpeakTextUseCase,
    private val stopSpeaking: StopSpeakingUseCase,
    private val shutdownTextToSpeech: ShutdownTextToSpeechUseCase
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
            is WorkoutUiEvent.StartTimer -> startTimer(event.durationSeconds)
            WorkoutUiEvent.PauseTimer -> pauseTimer()
            WorkoutUiEvent.ResumeTimer -> resumeTimer()
            WorkoutUiEvent.StopTimer -> stopTimer()
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
            speakText(textToRead)
            updateState { copy(isTTSActive = true) }
        }
    }

    private fun toggleTTSState() {
        if (uiState.isTTSActive) {
            stopSpeaking()
            shutdownTextToSpeech()
            updateState { copy(isTTSActive = false) }
        } else {
            updateState { copy(isTTSActive = true) }

        }
    }

    private fun startTimer(durationSeconds: Int) {
        cancelExistingTimer()

        updateState {
            copy(
                timerState = TimerState.RUNNING,
                timeRemaining = durationSeconds
            )
        }

        startCountdownTimer(durationSeconds)
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
        timerJob?.cancel()
        timerJob = null
        updateState { copy(timeRemaining = 0, timerState = TimerState.STOPPED) }
    }

    private fun startCountdownTimer(startTimeInSeconds: Int) {
        timerJob = viewModelScope.launch {
            for (second in startTimeInSeconds downTo 0) {
                updateState { copy(timeRemaining = second) }
                delay(1000L)
            }

            updateState { copy(timerState = TimerState.STOPPED) }
            nextWorkout()
        }
    }

    private fun cancelExistingTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        shutdownTextToSpeech()
    }

    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }

}
