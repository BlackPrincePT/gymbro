package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.WorkoutDto
import com.pegio.model.Workout
import javax.inject.Inject

class WorkoutDtoMapper @Inject constructor() : Mapper<WorkoutDto, Workout> {

    override fun mapToDomain(data: WorkoutDto): Workout = with(data) {
        return Workout(
            id = id ?: throw IllegalStateException(),
            authorId = authorId,
            title = title,
            description = description
        )
    }

    override fun mapFromDomain(data: Workout): WorkoutDto = with(data) {
        return WorkoutDto(
            id = id,
            authorId = authorId,
            title = title,
            description = description
        )
    }
}