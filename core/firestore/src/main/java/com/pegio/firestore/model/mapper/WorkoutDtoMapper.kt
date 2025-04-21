package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.WorkoutDto
import com.pegio.model.Workout
import javax.inject.Inject

class WorkoutDtoMapper @Inject constructor(): Mapper<WorkoutDto, Workout> {

    override fun mapToDomain(data: WorkoutDto): Workout {
        return Workout(
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

    override fun mapFromDomain(data: Workout): WorkoutDto {
        return WorkoutDto(
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
