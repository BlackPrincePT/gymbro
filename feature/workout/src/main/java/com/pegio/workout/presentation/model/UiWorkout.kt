package com.pegio.workout.presentation.model

import com.pegio.model.Workout.*

data class UiWorkout(
    val name: String,
    val description: String,
    val workoutType: WorkoutType,
    val value: Int,
    val sets: Int,
    val muscleGroups: List<MuscleGroup>,
    val workoutImage: String
)
