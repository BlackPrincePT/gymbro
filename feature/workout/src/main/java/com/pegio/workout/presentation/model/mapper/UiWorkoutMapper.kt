package com.pegio.workout.presentation.model.mapper

import com.pegio.common.core.FromDomainMapper
import com.pegio.model.Workout
import com.pegio.workout.presentation.model.UiWorkout
import javax.inject.Inject

class UiWorkoutMapper @Inject constructor() : FromDomainMapper<UiWorkout, Workout> {
    override fun mapFromDomain(data: Workout): UiWorkout {
        return UiWorkout(
            id = data.id,
            description = data.description,
            isFinished = data.isFinished,
            muscleGroups = data.muscleGroups,
            name = data.name,
            sets = data.sets,
            value = data.value,
            workoutImage = data.workoutImage,
            workoutType = data.workoutType
        )
    }
}