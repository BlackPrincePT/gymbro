package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import javax.inject.Inject

class ValidateWorkoutDescriptionUseCase @Inject constructor() {
    operator fun invoke(description: String): Resource<Unit, WorkoutValidationError.Description> {
        if (description.isBlank())
            return Resource.Failure(error = WorkoutValidationError.Description.EMPTY)

        if (description.length < 15)
            return Resource.Failure(error = WorkoutValidationError.Description.TOO_SHORT)

        return Resource.Success(Unit)
    }
}
