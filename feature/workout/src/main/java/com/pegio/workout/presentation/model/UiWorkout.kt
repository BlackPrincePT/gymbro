package com.pegio.workout.presentation.model

import com.pegio.model.Exercise.*
import java.util.UUID

data class UiWorkout(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val type: Type,
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
            type = Type.TIMED,
            value = 0,
            sets = 0,
            muscleGroups = emptyList(),
            workoutImage = ""
        )

    }

}
