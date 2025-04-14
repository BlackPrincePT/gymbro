package com.example.presentation.screen.workout_plan

import com.pegio.common.core.Error


sealed interface WorkoutPlanUiEffect {
    data class Failure(val error: Error) : WorkoutPlanUiEffect
    data object NavigateToAiChat: WorkoutPlanUiEffect
    data object NavigateBack: WorkoutPlanUiEffect
}