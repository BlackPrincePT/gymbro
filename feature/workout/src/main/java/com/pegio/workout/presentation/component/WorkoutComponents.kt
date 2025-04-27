package com.pegio.workout.presentation.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pegio.common.presentation.components.WorkoutImage
import com.pegio.model.Exercise.MuscleGroup
import com.pegio.model.Exercise.Type
import com.pegio.workout.presentation.model.UiExercise
import com.pegio.workout.presentation.screen.workout.WorkoutUiEvent
import com.pegio.workout.presentation.screen.workout.WorkoutUiState

@Composable
fun WorkoutDetailChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
        )
    }
}

@Composable
fun MuscleGroupsRow(muscleGroups: List<MuscleGroup>, modifier: Modifier = Modifier) {
    val muscleGroupsText = muscleGroups.joinToString(", ") { it.name }

    WorkoutDetailChip(
        icon = Icons.Default.Accessibility,
        text = muscleGroupsText,
        modifier = modifier
    )
}


@Composable
fun WorkoutDetailsCard(workout: UiExercise) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WorkoutDetailChip(
                    icon = Icons.Default.FitnessCenter,
                    text = when (workout.type) {
                        Type.REPETITION -> "${workout.value} reps"
                        Type.TIMED -> "${workout.value} seconds"
                    },
                    modifier = Modifier.weight(1f)
                )

                if (workout.sets > 1) {
                    WorkoutDetailChip(
                        icon = Icons.Default.Repeat,
                        text = "${workout.sets} sets",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (workout.muscleGroups.isNotEmpty()) {
                MuscleGroupsRow(muscleGroups = workout.muscleGroups)
            }
        }
    }
}

@Composable
fun WorkoutDetails(
    workout: UiExercise,
    onEvent: (WorkoutUiEvent) -> Unit,
    state: WorkoutUiState,
) {
    LaunchedEffect(workout.description, state.isTTSActive) {
        if (state.isTTSActive) {
            onEvent(WorkoutUiEvent.OnReadTTSClick(workout.description))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { onEvent(WorkoutUiEvent.OnToggleTTSClick) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = if (state.isTTSActive) Icons.AutoMirrored.Default.VolumeUp else Icons.AutoMirrored.Default.VolumeOff,
                contentDescription = if (state.isTTSActive) "Mute TTS" else "Read Description"
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 58.dp)
                .verticalScroll(rememberScrollState())
        ) {
            WorkoutImage(
                imageUrl = workout.workoutImage,
                contentDescription = workout.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )

            Text(
                text = workout.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            WorkoutDetailsCard(workout)

            TimerSection(
                workout = workout.type,
                workoutTime = workout.value,
                timeRemaining = state.timeRemaining,
                timerState = state.timerState,
                onPauseTimer = { onEvent(WorkoutUiEvent.PauseTimer) },
                onResumeTimer = { onEvent(WorkoutUiEvent.ResumeTimer) },
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (state.currentWorkoutIndex != 0) {
                    Button(
                        onClick = { onEvent(WorkoutUiEvent.OnPreviousClick) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Text("Back", style = MaterialTheme.typography.titleMedium)
                    }
                }

                Button(
                    onClick = { onEvent(WorkoutUiEvent.OnNextClick) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(
                        text = if (state.currentWorkoutIndex == state.workouts.size - 1) "Finish" else "Next",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWorkoutDetails() {
    val workout = UiExercise(
        type = Type.TIMED,
        value = 10,
        sets = 3,
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.ARMS),
        description = "A workout that targets the chest and arms.",
        name = "Push Up",
        workoutImage = ""
    )

    WorkoutDetails(
        workout = workout,
        onEvent = {},
        state = WorkoutUiState()
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewWorkoutDetailsCard() {
    val workout = UiExercise(
        type = Type.REPETITION,
        value = 10,
        sets = 3,
        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.ARMS),
        description = "",
        name = "",
        workoutImage = ""
    )

    WorkoutDetailsCard(workout = workout)
}

@Preview(showBackground = true)
@Composable
fun PreviewMuscleGroupsRow() {
    val muscleGroups = listOf(
        MuscleGroup.ARMS,
        MuscleGroup.CORE,
        MuscleGroup.FULL_BODY
    )

    MuscleGroupsRow(
        muscleGroups = muscleGroups,
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailChipPreview() {
    MaterialTheme {
        WorkoutDetailChip(
            icon = Icons.Default.FitnessCenter,
            text = "15 reps",
            modifier = Modifier.padding(16.dp)
        )
    }
}