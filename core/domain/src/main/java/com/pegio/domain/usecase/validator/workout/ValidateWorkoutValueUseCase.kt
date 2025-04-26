package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import javax.inject.Inject

class ValidateWorkoutValueUseCase @Inject constructor() {
    operator fun invoke(value: Int): Resource<Unit, WorkoutValidationError.Value> {
        if (value <= 0)
            return Resource.Failure(error = WorkoutValidationError.Value.INVALID)

        if (value < 5)
            return Resource.Failure(error = WorkoutValidationError.Value.TOO_LOW)

        return Resource.Success(Unit)
    }
}
