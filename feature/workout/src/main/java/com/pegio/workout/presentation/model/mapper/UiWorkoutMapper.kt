package com.pegio.workout.presentation.model.mapper

import com.pegio.common.core.FromDomainMapper
import com.pegio.common.core.Mapper
import com.pegio.model.Workout
import com.pegio.workout.presentation.model.UiWorkout
import javax.inject.Inject

class UiWorkoutMapper @Inject constructor() : Mapper<UiWorkout, Workout> {
    override fun mapFromDomain(data: Workout): UiWorkout {
        return UiWorkout(
            description = data.description,
            muscleGroups = data.muscleGroups,
            name = data.name,
            sets = data.sets,
            value = data.value,
            workoutImage = data.workoutImage,
            workoutType = data.workoutType
        )
    }

    override fun mapToDomain(data: UiWorkout): Workout {
        return Workout(
            description = data.description,
            muscleGroups = data.muscleGroups,
            name = data.name,
            sets = data.sets,
            value = data.value,
            workoutImage = data.workoutImage,
            workoutType = data.workoutType
        )
    }
}