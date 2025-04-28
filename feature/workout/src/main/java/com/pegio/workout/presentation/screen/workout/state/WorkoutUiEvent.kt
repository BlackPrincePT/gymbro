package com.pegio.workout.presentation.screen.workout.state

sealed interface WorkoutUiEvent {

    //Main buttons
    data object OnNextClick : WorkoutUiEvent
    data object OnPreviousClick : WorkoutUiEvent

    //TTS
    data class OnReadTTSClick(val textToRead: String) : WorkoutUiEvent
    data object OnToggleTTSClick : WorkoutUiEvent

    //Timer
    data object PauseTimer : WorkoutUiEvent
    data object ResumeTimer : WorkoutUiEvent

    // Navigation
    data object OnBackClick : WorkoutUiEvent
}