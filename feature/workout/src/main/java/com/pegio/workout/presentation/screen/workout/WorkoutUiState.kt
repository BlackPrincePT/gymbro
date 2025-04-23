package com.pegio.workout.presentation.screen.workout

import com.pegio.workout.presentation.model.UiWorkout

data class WorkoutUiState(
    val workouts: List<UiWorkout> = emptyList(),
    val currentWorkoutIndex: Int = 0,
    val isLoading: Boolean = false,
    val isTTSActive: Boolean = true,
    val timeRemaining: Int = 0,
    val timerState: TimerState = TimerState.STOPPED,
){
    enum class TimerState {
        RUNNING,
        PAUSED,
        STOPPED
    }
}
