package com.pegio.workout.presentation.model

import com.pegio.model.Exercise.MuscleGroup
import com.pegio.model.Exercise.Type
import java.util.UUID

data class UiExercise(
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
        val EMPTY = UiExercise(
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
