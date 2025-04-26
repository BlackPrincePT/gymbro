package com.pegio.model



data class Workout(
    val name: String,
    val description: String,
    val workoutType: WorkoutType,
    val value: Int,
    val sets: Int,
    val muscleGroups: List<MuscleGroup>,
    val workoutImage: String
) {
    enum class WorkoutType {
        TIMED,
        REPETITION
    }

    enum class MuscleGroup {
        CHEST,
        BACK,
        ARMS,
        SHOULDERS,
        BICEPS,
        TRICEPS,
        LEGS,
        CORE,
        FULL_BODY,
        CARDIO
    }
}
