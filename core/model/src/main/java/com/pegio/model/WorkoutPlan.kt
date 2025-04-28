package com.pegio.model


data class WorkoutPlan(
    val id: String,
    val level: DifficultyLevel,
    val title: String,
    val description: String,
    val duration: String,
    val intensity: String,
    val imageUrl: String,
    val workoutId: String
) {
    enum class DifficultyLevel {
        EASY,
        MEDIUM,
        HARD
    }
}
