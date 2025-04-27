package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.ExerciseDto
import com.pegio.model.Exercise
import javax.inject.Inject

class ExerciseDtoMapper @Inject constructor(): Mapper<ExerciseDto, Exercise> {

    override fun mapToDomain(data: ExerciseDto): Exercise {
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

    override fun mapFromDomain(data: Exercise): ExerciseDto {
        return ExerciseDto(
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