package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import javax.inject.Inject

class ValidateWorkoutSetsUseCase @Inject constructor() {
    operator fun invoke(sets: Int): Resource<Unit, WorkoutValidationError.Sets> {
        if (sets <= 0)
            return Resource.Failure(error = WorkoutValidationError.Sets.INVALID)

        if (sets > 10)
            return Resource.Failure(error = WorkoutValidationError.Sets.TOO_MANY)

        return Resource.Success(Unit)
    }
}
