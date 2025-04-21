package com.pegio.workout.presentation.screen.workout_plan

import androidx.annotation.StringRes

sealed interface WorkoutPlanUiEffect {
    data class Failure(@StringRes val errorRes: Int) : WorkoutPlanUiEffect
    data class NavigateToWorkout(val difficulty: String) : WorkoutPlanUiEffect
    data object NavigateToAiChat: WorkoutPlanUiEffect
    data object NavigateBack: WorkoutPlanUiEffect
}