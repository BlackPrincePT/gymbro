package com.pegio.workout.presentation.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.model.Exercise
import com.pegio.workout.presentation.model.UiWorkout
import javax.inject.Inject

class UiWorkoutMapper @Inject constructor() : Mapper<UiWorkout, Exercise> {
    override fun mapFromDomain(data: Exercise): UiWorkout {
        return UiWorkout(
            description = data.description,
            muscleGroups = data.muscleGroups,
            name = data.name,
            sets = data.sets,
            value = data.value,
            workoutImage = data.workoutImage,
            type = data.type
        )
    }

    override fun mapToDomain(data: UiWorkout): Exercise {
        return Exercise(
            description = data.description,
            muscleGroups = data.muscleGroups,
            name = data.name,
            sets = data.sets,
            value = data.value,
            workoutImage = data.workoutImage,
            type = data.type
        )
    }
}