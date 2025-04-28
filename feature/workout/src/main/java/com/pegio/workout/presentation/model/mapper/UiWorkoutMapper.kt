package com.pegio.workout.presentation.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.model.Workout
import com.pegio.workout.presentation.model.UiWorkout
import javax.inject.Inject

class UiWorkoutMapper @Inject constructor() : Mapper<UiWorkout, Workout> {
    override fun mapToDomain(data: UiWorkout): Workout = with(data) {
        return Workout(
            id = id,
            authorId = authorId,
            title = title,
            description = description
        )
    }

    override fun mapFromDomain(data: Workout): UiWorkout = with(data) {
        return UiWorkout(
            id = id,
            authorId = authorId,
            title = title,
            description = description
        )
    }
}