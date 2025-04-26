package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import javax.inject.Inject

class ValidateWorkoutImageUseCase @Inject constructor() {
    operator fun invoke(workoutImage: String): Resource<Unit, WorkoutValidationError.WorkoutImage> {
        if (workoutImage.isBlank())
            return Resource.Failure(error = WorkoutValidationError.WorkoutImage.EMPTY)

        return Resource.Success(Unit)
    }
}
