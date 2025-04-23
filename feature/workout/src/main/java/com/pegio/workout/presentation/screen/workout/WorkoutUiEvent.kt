package com.pegio.workout.presentation.screen.workout


sealed interface WorkoutUiEvent {
    //Workout fetch
    data class FetchWorkouts(val workoutId: String) : WorkoutUiEvent

    //Main buttons
    data object OnNextClick : WorkoutUiEvent
    data object OnPreviousClick : WorkoutUiEvent

    //TTS
    data class OnReadTTSClick(val textToRead: String) : WorkoutUiEvent
    data object OnToggleTTSClick : WorkoutUiEvent

    //Timer
    data class StartTimer(val durationSeconds: Int) : WorkoutUiEvent
    data object PauseTimer : WorkoutUiEvent
    data object ResumeTimer : WorkoutUiEvent
    data object StopTimer : WorkoutUiEvent

    // Navigation
    data object OnBackClick : WorkoutUiEvent
}