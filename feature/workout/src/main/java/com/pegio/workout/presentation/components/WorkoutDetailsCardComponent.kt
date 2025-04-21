package com.pegio.workout.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pegio.model.Workout.WorkoutType
import com.pegio.workout.presentation.model.UiWorkout


@Composable
fun WorkoutDetailsCard(workout: UiWorkout) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WorkoutDetailChip(
                    icon = Icons.Default.FitnessCenter,
                    text = when (workout.workoutType) {
                        WorkoutType.REPETITION -> "${workout.value} reps"
                        WorkoutType.TIMED -> "${workout.value} seconds"
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