package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.ExerciseDto
import com.pegio.model.Workout
import javax.inject.Inject

class ExerciseDtoMapper @Inject constructor(): Mapper<ExerciseDto, Workout> {

    override fun mapToDomain(data: ExerciseDto): Workout {
        return Workout(
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

    override fun mapFromDomain(data: Workout): ExerciseDto {
        return ExerciseDto(
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