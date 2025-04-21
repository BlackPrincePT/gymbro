package com.pegio.workout.presentation.screen.workout

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.component.WorkoutImage
import com.pegio.model.Workout.MuscleGroup
import com.pegio.model.Workout.WorkoutType
import com.pegio.workout.presentation.model.UiWorkout


@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    workoutPlanId: String,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onSetupTopBar: (TopBarState) -> Unit
) {

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (viewModel.uiState.workouts.isEmpty()) {
            viewModel.onEvent(WorkoutUiEvent.FetchWorkouts(workoutPlanId))
        }
    }

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is WorkoutUiEffect.Failure -> onShowSnackbar(context.getString(effect.errorRes), null)
            WorkoutUiEffect.NavigateBack -> onBackClick()
        }
    }

    WorkoutContent(
        state = viewModel.uiState,
        onNextClick = { viewModel.onEvent(WorkoutUiEvent.OnNextClick) }
    )
}


@Composable
fun WorkoutContent(
    state: WorkoutUiState,
    onNextClick: () -> Unit
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val currentWorkout = state.workouts.getOrNull(state.currentWorkoutIndex)
        currentWorkout?.let {
            WorkoutDetails(
                workout = it,
                onNextClick = onNextClick,
                isLastWorkout = state.currentWorkoutIndex == state.workouts.size - 1
            )
        }
    }
}

@Composable
fun WorkoutDetails(
    workout: UiWorkout,
    onNextClick: () -> Unit,
    isLastWorkout: Boolean
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        WorkoutImage(
            imageUrl = workout.workoutImage,
            contentDescription = workout.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(12.dp)),
        )

        Text(
            text = workout.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        WorkoutDetailsCard(workout)


        Text(
            text = workout.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isLastWorkout) "Finish" else "Next",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

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
fun WorkoutDetailChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
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
}



@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (WorkoutUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(WorkoutUiEvent.OnBackClick) }
                )
            )
        )
    }
}