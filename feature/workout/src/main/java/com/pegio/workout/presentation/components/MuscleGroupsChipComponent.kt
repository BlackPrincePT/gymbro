package com.pegio.workout.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pegio.model.Workout.MuscleGroup

@Composable
fun MuscleGroupsRow(muscleGroups: List<MuscleGroup>, modifier: Modifier = Modifier) {
    val muscleGroupsText = muscleGroups.joinToString(", ") { it.name }

    WorkoutDetailChip(
        icon = Icons.Default.Accessibility,
        text = muscleGroupsText,
        modifier = modifier
    )
}

