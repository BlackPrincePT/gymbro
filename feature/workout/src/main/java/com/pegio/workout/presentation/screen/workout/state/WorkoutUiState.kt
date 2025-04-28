package com.pegio.workout.presentation.screen.workout.state

import com.pegio.workout.presentation.model.UiExercise

data class WorkoutUiState(
    val workouts: List<UiExercise> = emptyList(),
    val currentWorkoutIndex: Int = 0,
    val isLoading: Boolean = false,
    val isTTSActive: Boolean = false,
    val timeRemaining: Int = 0,
    val timerState: TimerState = TimerState.NOT_STARTED,
){
    enum class TimerState {
        NOT_STARTED,
        RUNNING,
        PAUSED
    }
}
