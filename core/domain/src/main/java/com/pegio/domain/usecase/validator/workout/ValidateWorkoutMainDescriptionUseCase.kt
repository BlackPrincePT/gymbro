package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import javax.inject.Inject

class ValidateWorkoutMainDescriptionUseCase @Inject constructor() {
    operator fun invoke(name: String): Resource<Unit, WorkoutValidationError.WorkoutDescription> {
        if (name.isBlank())
            return Resource.Failure(error = WorkoutValidationError.WorkoutDescription.EMPTY)

        if (name.length < 3)
            return Resource.Failure(error = WorkoutValidationError.WorkoutDescription.TOO_SHORT)

        if (name.length > 15)
            return Resource.Failure(error = WorkoutValidationError.WorkoutDescription.TOO_LONG)

        return Resource.Success(Unit)
    }
}