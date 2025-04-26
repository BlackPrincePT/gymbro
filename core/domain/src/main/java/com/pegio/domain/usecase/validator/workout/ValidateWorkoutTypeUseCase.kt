package com.pegio.domain.usecase.validator.workout

import com.pegio.common.core.Resource
import com.pegio.common.core.WorkoutValidationError
import com.pegio.model.Workout.WorkoutType
import javax.inject.Inject

class ValidateWorkoutTypeUseCase @Inject constructor() {
    operator fun invoke(workoutType: WorkoutType?): Resource<Unit, WorkoutValidationError.WorkoutType> {
        if (workoutType == null)
            return Resource.Failure(error = WorkoutValidationError.WorkoutType.EMPTY)

        return Resource.Success(Unit)
    }
}
