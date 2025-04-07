package com.pegio.gymbro.presentation.screen.workout_plan

import com.pegio.gymbro.domain.core.Error

sealed interface WorkoutPlanUiEffect {
    data class Failure(val error: Error) : WorkoutPlanUiEffect
}