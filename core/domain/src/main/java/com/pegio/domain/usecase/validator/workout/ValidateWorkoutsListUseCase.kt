package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import com.pegio.model.Workout
import javax.inject.Inject

class ValidateWorkoutsListUseCase @Inject constructor() {
    operator fun invoke(workouts: List<Workout>): Resource<Unit, WorkoutValidationError.Workouts> {
        if (workouts.size < 5) {
            return Resource.Failure(error = WorkoutValidationError.Workouts.TOO_FEW)
        }
        else if (workouts.size > 30){
            return Resource.Failure(error = WorkoutValidationError.Workouts.TOO_MANY)
        }
        return Resource.Success(Unit)
    }
}