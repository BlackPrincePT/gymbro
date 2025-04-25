package com.pegio.workout.presentation.model

import com.pegio.model.Workout.*
import java.util.UUID

data class UiWorkout(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val workoutType: WorkoutType,
    val value: Int,
    val sets: Int,
    val muscleGroups: List<MuscleGroup>,
    val workoutImage: String
){

    companion object {
        val EMPTY = UiWorkout(
            id = "",
            name = "",
            description = "",
            workoutType = WorkoutType.TIMED,
            value = 0,
            sets = 0,
            muscleGroups = emptyList(),
            workoutImage = ""
        )

    }

}
