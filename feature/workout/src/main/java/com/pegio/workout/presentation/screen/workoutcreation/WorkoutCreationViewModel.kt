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
import com.pegio.workout.presentation.model.mapper.UiExerciseMapper
import com.pegio.workout.presentation.screen.workoutcreation.state.WorkoutCreationUiEffect
import com.pegio.workout.presentation.screen.workoutcreation.state.WorkoutCreationUiEvent
import com.pegio.workout.presentation.screen.workoutcreation.state.WorkoutCreationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WorkoutCreationViewModel @Inject constructor(
    private val workoutFormValidator: WorkoutFormValidatorUseCases,
    private val uploadExercise: UploadExerciseUseCase,
    private val uploadWorkout: UploadWorkoutUseCase,
    private val uiExerciseMapper: UiExerciseMapper
) : BaseViewModel<WorkoutCreationUiState, WorkoutCreationUiEffect, WorkoutCreationUiEvent>(
    initialState = WorkoutCreationUiState()
) {
    override fun onEvent(event: WorkoutCreationUiEvent) {
        when (event) {
            // Workout actions
            WorkoutCreationUiEvent.OnAddWorkoutClick -> showAddWorkoutDialog()
            WorkoutCreationUiEvent.OnUploadWorkouts -> uploadWorkouts()
            is WorkoutCreationUiEvent.RemoveWorkout -> removeWorkout(event.workoutId)
            is WorkoutCreationUiEvent.OnEditWorkout -> editWorkout(event.workout)

            // Workout Dialog
            WorkoutCreationUiEvent.OnDismissDialog -> hideAddWorkoutDialog()
            WorkoutCreationUiEvent.OnSaveWorkout -> saveWorkout()

            // Fields
            is WorkoutCreationUiEvent.OnDescriptionChange -> updateState { copy(description = event.description) }
            is WorkoutCreationUiEvent.OnTitleChange -> updateState { copy(title = event.title) }

            // Navigation
            WorkoutCreationUiEvent.OnBackClick -> sendEffect(WorkoutCreationUiEffect.NavigateBack)
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



    private fun areWorkoutFieldsValid(): Boolean = with(uiState.newWorkout) {
        val nameError = workoutFormValidator.validateWorkoutName(name).errorOrNull()
        val descriptionError = workoutFormValidator.validateWorkoutDescription(description).errorOrNull()
        val valueError = workoutFormValidator.validateWorkoutValue(value).errorOrNull()
        val setsError = workoutFormValidator.validateWorkoutSets(sets).errorOrNull()
        val muscleGroupsError = workoutFormValidator.validateWorkoutMuscleGroups(muscleGroups).errorOrNull()
        val workoutImageError = workoutFormValidator.validateWorkoutImage(workoutImage).errorOrNull()

        updateState {
            copy(
                validationError = validationError.copy(
                    name = nameError?.toStringResId(),
                    description = descriptionError?.toStringResId(),
                    value = valueError?.toStringResId(),
                    sets = setsError?.toStringResId(),
                    muscleGroups = muscleGroupsError?.toStringResId(),
                    workoutImage = workoutImageError?.toStringResId()
                )
            )
        }

        return nameError == null && descriptionError == null && valueError == null && setsError == null && muscleGroupsError == null && workoutImageError == null
    }


    private fun uploadWorkouts() {
        workoutFormValidator.validateWorkoutsListUseCase(
            uiState.exercises.map { uiWorkout ->
                uiExerciseMapper.mapToDomain(uiWorkout)
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
        if (!validateWorkoutFields()) {
            return
        }

        launchWithLoading {
            retryableCall {
                val workoutsToUpload = uiState.exercises.map { uiWorkout ->
                    uiExerciseMapper.mapToDomain(uiWorkout)
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

    private fun validateWorkoutFields(): Boolean {
        val titleError = workoutFormValidator.validateWorkoutTitle(uiState.title).errorOrNull()
        val mainDescriptionError = workoutFormValidator.validateWorkoutMainDescription(uiState.description).errorOrNull()

        updateState {
            copy(
                validationError = validationError.copy(
                    title = titleError?.toStringResId(),
                    mainDescription = mainDescriptionError?.toStringResId()
                )
            )
        }

        return titleError == null && mainDescriptionError == null
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