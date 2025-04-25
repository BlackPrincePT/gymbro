package com.pegio.workout.presentation.screen.workoutcreation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.model.Workout.MuscleGroup
import com.pegio.model.Workout.WorkoutType
import com.pegio.workout.presentation.model.UiWorkout

@Composable
fun WorkoutCreationScreen(
    viewModel: WorkoutCreationViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onSetupTopBar: (TopBarState) -> Unit
) {

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    val context = LocalContext.current

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is WorkoutCreationUiEffect.Failure -> onShowSnackbar(context.getString(effect.errorRes), null)
            WorkoutCreationUiEffect.NavigateBack -> onBackClick()
        }
    }

    WorkoutCreationContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun WorkoutCreationContent(
    state: WorkoutCreationUiState,
    onEvent: (WorkoutCreationUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(state.workouts) { workout ->
                WorkoutItem(
                    workout = workout,
                    onRemoveClick = { onEvent(WorkoutCreationUiEvent.RemoveWorkout(workout.id)) },
                    onClick = { onEvent(WorkoutCreationUiEvent.OnEditWorkout(workout)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        FloatingActionButton(
            onClick = { onEvent(WorkoutCreationUiEvent.OnAddWorkoutClick) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Workout")
        }

        if (state.showAddWorkoutDialog && state.newWorkout != null) {
            EnhancedAddWorkoutDialog(
                workout = state.newWorkout,
                onSave = { onEvent(WorkoutCreationUiEvent.OnSaveWorkout) },
                onDismiss = { onEvent(WorkoutCreationUiEvent.OnDismissDialog) },
                onWorkoutChange = { onEvent(WorkoutCreationUiEvent.OnEditWorkout(it)) }
            )
        }
    }
}

@Composable
fun WorkoutItem(
    workout: UiWorkout,
    onRemoveClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
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
                    contentDescription = "Workout Image",
                    modifier = Modifier
                        .size(100.dp) // Increased image size
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
                            text = workout.workoutType.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        val valueText = when (workout.workoutType) {
                            WorkoutType.TIMED -> "${workout.value} seconds"
                            WorkoutType.REPETITION -> "${workout.value} reps"
                        }

                        Text(
                            text = valueText,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${workout.sets} sets",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Workout"
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
fun EnhancedAddWorkoutDialog(
    workout: UiWorkout,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    onWorkoutChange: (UiWorkout) -> Unit
) {
    var workoutTypeExpanded by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            onWorkoutChange(workout.copy(workoutImage = uri.toString()))
        }
    }

    val isFormValid = workout.name.isNotBlank()
            && workout.description.isNotBlank()
            && workout.workoutImage.isNotEmpty()
            && workout.value > 0
            && workout.sets > 0


    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add New Workout",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (workout.workoutImage.isNotEmpty()) {
                        AsyncImage(
                            model = workout.workoutImage,
                            contentDescription = "Workout Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Select Image",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Select Workout Image",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = workout.name,
                    onValueChange = { newName ->
                        onWorkoutChange(workout.copy(name = newName))
                    },
                    label = { Text("Workout Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = workout.description,
                    onValueChange = { newDescription ->
                        onWorkoutChange(workout.copy(description = newDescription))
                    },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = workoutTypeExpanded,
                    onExpandedChange = { workoutTypeExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = workout.workoutType.toString(),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Workout Type") },
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
                        WorkoutType.entries.forEach { workoutType ->
                            DropdownMenuItem(
                                text = { Text(workoutType.toString()) },
                                onClick = {
                                    onWorkoutChange(workout.copy(
                                        workoutType = workoutType,
                                        value = 0
                                    ))
                                    workoutTypeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when (workout.workoutType) {
                    WorkoutType.TIMED -> {
                        OutlinedTextField(
                            value = if (workout.value == 0) "" else workout.value.toString(),
                            onValueChange = { newValue ->
                                onWorkoutChange(workout.copy(value = newValue.toIntOrNull() ?: 0))
                            },
                            label = { Text("Time (seconds)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    WorkoutType.REPETITION -> {
                        OutlinedTextField(
                            value = if (workout.value == 0) "" else workout.value.toString(),
                            onValueChange = { newValue ->
                                onWorkoutChange(workout.copy(value = newValue.toIntOrNull() ?: 0))
                            },
                            label = { Text("Repetitions") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (workout.sets == 0) "" else workout.sets.toString(),
                    onValueChange = { newSets ->
                        onWorkoutChange(workout.copy(sets = newSets.toIntOrNull() ?: 0))
                    },
                    label = { Text("Sets") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Target Muscle Groups",
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
                        val isSelected = muscleGroup in workout.muscleGroups
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val newSelection = if (isSelected) {
                                    workout.muscleGroups - muscleGroup
                                } else {
                                    workout.muscleGroups + muscleGroup
                                }
                                onWorkoutChange(workout.copy(muscleGroups = newSelection))
                            },
                            label = { Text(muscleGroup.name.replace("_", " ")) }
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onSave,
                        enabled = isFormValid
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (WorkoutCreationUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(WorkoutCreationUiEvent.OnBackClick) }
                )
            )
        )
    }
}