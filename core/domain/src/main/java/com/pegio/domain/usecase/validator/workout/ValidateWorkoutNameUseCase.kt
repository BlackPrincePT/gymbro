package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import javax.inject.Inject

class ValidateWorkoutNameUseCase @Inject constructor() {
    operator fun invoke(name: String): Resource<Unit, WorkoutValidationError.Name> {
        if (name.isBlank())
            return Resource.Failure(error = WorkoutValidationError.Name.EMPTY)

        if (name.length < 3)
            return Resource.Failure(error = WorkoutValidationError.Name.TOO_SHORT)

        return Resource.Success(Unit)
    }
}
