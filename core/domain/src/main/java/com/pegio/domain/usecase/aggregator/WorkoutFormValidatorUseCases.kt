package com.pegio.domain.usecase.aggregator

import com.pegio.domain.usecase.validator.workout.ValidateWorkoutDescriptionUseCase
import com.pegio.domain.usecase.validator.workout.ValidateWorkoutImageUseCase
import com.pegio.domain.usecase.validator.workout.ValidateWorkoutMuscleGroupsUseCase
import com.pegio.domain.usecase.validator.workout.ValidateWorkoutNameUseCase
import com.pegio.domain.usecase.validator.workout.ValidateWorkoutSetsUseCase
import com.pegio.domain.usecase.validator.workout.ValidateWorkoutValueUseCase
import javax.inject.Inject

data class WorkoutFormValidatorUseCases @Inject constructor(
    val validateWorkoutName: ValidateWorkoutNameUseCase,
    val validateWorkoutDescription: ValidateWorkoutDescriptionUseCase,
    val validateWorkoutValue: ValidateWorkoutValueUseCase,
    val validateWorkoutSets: ValidateWorkoutSetsUseCase,
    val validateWorkoutMuscleGroups: ValidateWorkoutMuscleGroupsUseCase,
    val validateWorkoutImage: ValidateWorkoutImageUseCase
)
