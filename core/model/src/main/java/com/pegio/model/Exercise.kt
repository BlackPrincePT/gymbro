package com.pegio.model

data class Exercise(
    val name: String,
    val description: String,
    val type: Type,
    val value: Int,
    val position: Int,
    val sets: Int,
    val muscleGroups: List<MuscleGroup>,
    val workoutImage: String
) {
    enum class Type {
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
