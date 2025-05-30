package com.pegio.workout.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.pegio.designsystem.component.FormTextField
import com.pegio.model.Exercise.MuscleGroup
import com.pegio.model.Exercise.Type
import com.pegio.workout.R
import com.pegio.workout.presentation.model.UiExercise
import com.pegio.workout.presentation.screen.workoutcreation.state.WorkoutCreationUiEvent
import com.pegio.workout.presentation.screen.workoutcreation.state.WorkoutCreationUiState


@Composable
fun WorkoutItem(
    workout: UiExercise,
    onEvent: (WorkoutCreationUiEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onEvent(WorkoutCreationUiEvent.OnEditWorkout(workout)) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = workout.workoutImage,
                    contentDescription = stringResource(R.string.feature_workout_workout_image),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workout.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = workout.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = workout.type.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        val valueText = when (workout.type) {
                            Type.TIMED -> stringResource(
                                R.string.feature_workout_seconds,
                                workout.value
                            )
                            Type.REPETITION -> stringResource(
                                R.string.feature_workout_reps,
                                workout.value
                            )
                        }

                        Text(
                            text = valueText,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = stringResource(
                                R.string.feature_workout_sets,
                                workout.value
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                IconButton(onClick = { onEvent(WorkoutCreationUiEvent.RemoveWorkout(workout.id)) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.feature_workout_remove_workout)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (workout.muscleGroups.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    workout.muscleGroups.forEach { muscle ->
                        FilterChip(
                            selected = true,
                            onClick = {},
                            enabled = false,
                            label = {
                                Icon(
                                    imageVector = Icons.Default.Accessibility,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(muscle.name.replace("_", " "))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddWorkoutDialog(
    state: WorkoutCreationUiState,
    onEvent: (WorkoutCreationUiEvent) -> Unit
) {
    var workoutTypeExpanded by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = { onEvent(WorkoutCreationUiEvent.OnDismissDialog) }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.feature_workout_add_new_workout),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onEvent ( WorkoutCreationUiEvent.OnLaunchGallery) },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.newWorkout.workoutImage.isNotEmpty()) {
                        AsyncImage(
                            model = state.newWorkout.workoutImage,
                            contentDescription = stringResource(R.string.feature_workout_workout_image),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = stringResource(R.string.feature_workout_select_image),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.feature_workout_select_workout_image),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                state.validationError.workoutImage?.let{
                    Text(
                        text = stringResource(state.validationError.workoutImage),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = state.newWorkout.name,

                    onValueChange = { newName ->
                        onEvent(
                            WorkoutCreationUiEvent.OnEditWorkout(
                                state.newWorkout.copy(name = newName)
                            )
                        )
                    },
                    label = stringResource(R.string.feature_workout_workout_name),
                    error = state.validationError.name,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))


                FormTextField(
                    value = state.newWorkout.description,
                    onValueChange = { newDescription ->
                        onEvent(
                            WorkoutCreationUiEvent.OnEditWorkout(
                                state.newWorkout.copy(description = newDescription)
                            )
                        )
                    },
                    label = stringResource(R.string.feature_workout_description),
                    error = state.validationError.description,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = workoutTypeExpanded,
                    onExpandedChange = { workoutTypeExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.newWorkout.type.toString(),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(R.string.feature_workout_workout_type)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = workoutTypeExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = workoutTypeExpanded,
                        onDismissRequest = { workoutTypeExpanded = false }
                    ) {
                        Type.entries.forEach { workoutType ->
                            DropdownMenuItem(
                                text = { Text(workoutType.toString()) },
                                onClick = {
                                    onEvent(
                                        WorkoutCreationUiEvent.OnEditWorkout(
                                            state.newWorkout.copy(
                                                type = workoutType,
                                                value = 0
                                            )
                                        )
                                    )
                                    workoutTypeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when (state.newWorkout.type) {
                    Type.TIMED -> {
                        FormTextField(
                            value = if (state.newWorkout.value == 0) "" else state.newWorkout.value.toString(),
                            onValueChange = { newValue ->
                                onEvent(
                                    WorkoutCreationUiEvent.OnEditWorkout(
                                        state.newWorkout.copy(
                                            value = newValue.toIntOrNull() ?: 0
                                        )
                                    )
                                )
                            },
                            label = stringResource(R.string.feature_workout_time_seconds),
                            error = state.validationError.value,
                            isNumberOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Type.REPETITION -> {
                        FormTextField(
                            value = if (state.newWorkout.value == 0) "" else state.newWorkout.value.toString(),
                            onValueChange = { newValue ->
                                onEvent(
                                    WorkoutCreationUiEvent.OnEditWorkout(
                                        state.newWorkout.copy(
                                            value = newValue.toIntOrNull() ?: 0
                                        )
                                    )
                                )
                            },
                            label = stringResource(R.string.feature_workout_repetitions),
                            error = state.validationError.value,
                            isNumberOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                FormTextField(
                    value = if (state.newWorkout.sets == 0) "" else state.newWorkout.sets.toString(),
                    onValueChange = { newSets ->
                        onEvent(
                            WorkoutCreationUiEvent.OnEditWorkout(
                                state.newWorkout.copy(
                                    sets = newSets.toIntOrNull() ?: 0
                                )
                            )
                        )
                    },
                    label = stringResource(R.string.feature_workout_just_sets),
                    error = state.validationError.sets,
                    isNumberOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = stringResource(R.string.feature_workout_target_muscle_groups),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MuscleGroup.entries.forEach { muscleGroup ->
                        val isSelected = muscleGroup in state.newWorkout.muscleGroups
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val newSelection = if (isSelected) {
                                    state.newWorkout.muscleGroups - muscleGroup
                                } else {
                                    state.newWorkout.muscleGroups + muscleGroup
                                }
                                onEvent(
                                    WorkoutCreationUiEvent.OnEditWorkout(
                                        state.newWorkout.copy(
                                            muscleGroups = newSelection
                                        )
                                    )
                                )
                            },
                            label = { Text(muscleGroup.name.replace("_", " ")) }
                        )
                    }
                }

                state.validationError.muscleGroups?.let{
                    Text(
                        text =  stringResource(state.validationError.muscleGroups),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onEvent(WorkoutCreationUiEvent.OnDismissDialog) }) {
                        Text(stringResource(R.string.feature_workout_cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onEvent(WorkoutCreationUiEvent.OnSaveWorkout) },
                    ) {
                        Text(stringResource(R.string.feature_workout_save))
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWorkoutItem() {
    WorkoutItem(
        workout = UiExercise.EMPTY,
        onEvent = {}
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewAddWorkoutDialog() {
    AddWorkoutDialog(
        state = WorkoutCreationUiState(),
        onEvent = {}
    )
}
