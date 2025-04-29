package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import javax.inject.Inject

class ValidateWorkoutTitleUseCase @Inject constructor() {
    operator fun invoke(name: String): Resource<Unit, WorkoutValidationError.WorkoutTitle> {
        if (name.isBlank())
            return Resource.Failure(error = WorkoutValidationError.WorkoutTitle.EMPTY)

        if (name.length < 3)
            return Resource.Failure(error = WorkoutValidationError.WorkoutTitle.TOO_SHORT)

        if (name.length > 50)
            return Resource.Failure(error = WorkoutValidationError.WorkoutTitle.TOO_LONG)

        return Resource.Success(Unit)
    }
}