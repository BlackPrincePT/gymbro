package com.pegio.workout.presentation.model

data class UiWorkoutPlan(
    val id: String,
    val title: String,
    val description: String,
    val duration: String,
    val intensity: String,
    val difficulty: String,
    val imageUrl: String
)