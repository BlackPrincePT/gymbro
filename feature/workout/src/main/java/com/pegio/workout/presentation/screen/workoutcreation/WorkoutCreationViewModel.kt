package com.pegio.workout.presentation.screen.workoutcreation

import com.pegio.common.core.errorOrNull
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.core.retryableCall
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.aggregator.WorkoutFormValidatorUseCases
import com.pegio.domain.usecase.workout.UploadExerciseUseCase
import com.pegio.domain.usecase.workout.UploadWorkoutUseCase
import com.pegio.workout.presentation.model.UiExercise
import com.pegio.workout.presentation.model.mapper.UiWorkoutMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WorkoutCreationViewModel @Inject constructor(
    private val workoutFormValidator: WorkoutFormValidatorUseCases,
    private val uploadExercise: UploadExerciseUseCase,
    private val uploadWorkout: UploadWorkoutUseCase,
    private val uiWorkoutMapper: UiWorkoutMapper
) : BaseViewModel<WorkoutCreationUiState, WorkoutCreationUiEffect, WorkoutCreationUiEvent>(
    initialState = WorkoutCreationUiState()
) {
    override fun onEvent(event: WorkoutCreationUiEvent) {
        when (event) {
            is WorkoutCreationUiEvent.RemoveWorkout -> removeWorkout(event.workoutId)
            is WorkoutCreationUiEvent.OnEditWorkout -> editWorkout(event.workout)
            WorkoutCreationUiEvent.OnBackClick -> sendEffect(WorkoutCreationUiEffect.NavigateBack)
            WorkoutCreationUiEvent.OnAddWorkoutClick -> showAddWorkoutDialog()
            WorkoutCreationUiEvent.OnDismissDialog -> hideAddWorkoutDialog()
            WorkoutCreationUiEvent.OnSaveWorkout -> saveWorkout()
            WorkoutCreationUiEvent.OnUploadWorkouts -> uploadWorkouts()
            is WorkoutCreationUiEvent.OnDescriptionChange -> updateState { copy(description = event.description) }
            is WorkoutCreationUiEvent.OnTitleChange -> updateState { copy(title = event.title) }
        }
    }

    private fun showAddWorkoutDialog() {
        updateState {
            copy(
                showAddWorkoutDialog = true,
                newWorkout = UiExercise.EMPTY.copy(id = UUID.randomUUID().toString())
            )
        }
    }

    private fun editWorkout(workout: UiExercise) {
        updateState {
            copy(
                newWorkout = workout,
                showAddWorkoutDialog = true
            )
        }
    }


    private fun hideAddWorkoutDialog() {
        updateState {
            copy(
                showAddWorkoutDialog = false,
            )
        }
    }

    private fun saveWorkout() {
        if (areWorkoutFieldsValid()) {
            val workout = uiState.newWorkout

            updateState {
                val updatedList = exercises.toMutableList()

                val index = updatedList.indexOfFirst { it.id == workout.id }

                if (index >= 0) {
                    updatedList[index] = workout.copy(position = updatedList[index].position)
                } else {
                    val newPosition = updatedList.size
                    updatedList.add(workout.copy(position = newPosition))
                }

                copy(
                    exercises = updatedList,
                    showAddWorkoutDialog = false
                )
            }
        }
    }



    private fun areWorkoutFieldsValid(): Boolean {
        val currentWorkout = uiState.newWorkout

        var isValid = true

        workoutFormValidator.validateWorkoutName(currentWorkout.name)
            .errorOrNull()
            .let {
                updateState {
                    copy(validationError = validationError.copy(name = it?.toStringResId()))
                }
                isValid = it == null
            }

        workoutFormValidator.validateWorkoutDescription(currentWorkout.description)
            .errorOrNull()
            .let {
                updateState {
                    copy(validationError = validationError.copy(description = it?.toStringResId()))
                }
                isValid = isValid && it == null
            }

        workoutFormValidator.validateWorkoutValue(currentWorkout.value)
            .errorOrNull()
            .let {
                updateState {
                    copy(validationError = validationError.copy(value = it?.toStringResId()))
                }
                isValid = isValid && it == null
            }

        workoutFormValidator.validateWorkoutSets(currentWorkout.sets)
            .errorOrNull()
            .let {
                updateState {
                    copy(validationError = validationError.copy(sets = it?.toStringResId()))
                }
                isValid = isValid && it == null
            }

        workoutFormValidator.validateWorkoutMuscleGroups(currentWorkout.muscleGroups)
            .errorOrNull()
            .let {
                updateState {
                    copy(validationError = validationError.copy(muscleGroups = it?.toStringResId()))
                }
                isValid = isValid && it == null
            }

        workoutFormValidator.validateWorkoutImage(currentWorkout.workoutImage)
            .errorOrNull()
            .let {
                updateState {
                    copy(validationError = validationError.copy(workoutImage = it?.toStringResId()))
                }
                isValid = isValid && it == null
            }

        return isValid
    }


    private fun uploadWorkouts() {
        workoutFormValidator.validateWorkoutsListUseCase(
            uiState.exercises.map { uiWorkout ->
                uiWorkoutMapper.mapToDomain(uiWorkout)
            }
        )
            .onFailure {
                sendEffect(
                    WorkoutCreationUiEffect.Failure(
                        errorRes = it.toStringResId()
                    )
                )
                return
            }

        launchWithLoading {
            retryableCall {
                val workoutsToUpload = uiState.exercises.map { uiWorkout ->
                    uiWorkoutMapper.mapToDomain(uiWorkout)
                }
                uploadWorkout(title = uiState.title, description = uiState.description).onSuccess {
                    uploadExercise(workoutsToUpload, workoutId = it.id)
                }
            }
                .onSuccess {
                    sendEffect(WorkoutCreationUiEffect.NavigateBack)
                }
                .onFailure { error ->
                    sendEffect(WorkoutCreationUiEffect.Failure(error.toStringResId()))
                }
        }
    }


    private fun removeWorkout(workoutId: String) {
        updateState {
            copy(exercises = exercises.filter { it.id != workoutId })
        }
    }

    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}