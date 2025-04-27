package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import com.pegio.model.Exercise.MuscleGroup
import javax.inject.Inject

class ValidateWorkoutMuscleGroupsUseCase @Inject constructor() {
    operator fun invoke(muscleGroups: List<MuscleGroup>): Resource<Unit, WorkoutValidationError.MuscleGroups> {
        if (muscleGroups.isEmpty())
            return Resource.Failure(error = WorkoutValidationError.MuscleGroups.EMPTY)

        return Resource.Success(Unit)
    }
}
