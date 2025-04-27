package com.pegio.workout.presentation.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.model.Exercise
import com.pegio.workout.presentation.model.UiExercise
import javax.inject.Inject

class UiWorkoutMapper @Inject constructor() : Mapper<UiExercise, Exercise> {
    override fun mapFromDomain(data: Exercise): UiExercise {
        return UiExercise(
            description = data.description,
            muscleGroups = data.muscleGroups,
            name = data.name,
            sets = data.sets,
            value = data.value,
            workoutImage = data.workoutImage,
            type = data.type,
            position = data.position
        )
    }

    override fun mapToDomain(data: UiExercise): Exercise {
        return Exercise(
            description = data.description,
            muscleGroups = data.muscleGroups,
            name = data.name,
            sets = data.sets,
            value = data.value,
            workoutImage = data.workoutImage,
            type = data.type,
            position = data.position

        )
    }
}