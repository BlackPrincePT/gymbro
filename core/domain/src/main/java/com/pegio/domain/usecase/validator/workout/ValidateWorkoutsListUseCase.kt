package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import com.pegio.model.Exercise
import javax.inject.Inject

class ValidateWorkoutsListUseCase @Inject constructor() {
    operator fun invoke(exercises: List<Exercise>): Resource<Unit, WorkoutValidationError.Workouts> {
        if (exercises.size < 5) {
            return Resource.Failure(error = WorkoutValidationError.Workouts.TOO_FEW)
        }
        else if (exercises.size > 30){
            return Resource.Failure(error = WorkoutValidationError.Workouts.TOO_MANY)
        }
        return Resource.Success(Unit)
    }
}