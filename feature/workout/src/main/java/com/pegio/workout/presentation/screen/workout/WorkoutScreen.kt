package com.pegio.workout.presentation.screen.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowHardware
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.model.Exercise
import com.pegio.workout.presentation.component.WorkoutDetails
import com.pegio.workout.presentation.model.UiExercise
import com.pegio.workout.presentation.screen.workout.state.WorkoutUiEffect
import com.pegio.workout.presentation.screen.workout.state.WorkoutUiEvent
import com.pegio.workout.presentation.screen.workout.state.WorkoutUiState


@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {

    SetupTopBar(onSetupTopBar, viewModel::onEvent, viewModel.uiState)

    val context = LocalContext.current


    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is WorkoutUiEffect.Failure -> onShowSnackbar(context.getString(effect.errorRes))
            WorkoutUiEffect.NavigateBack -> onBackClick()
        }
    }


    WorkoutContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun WorkoutContent(
    state: WorkoutUiState,
    onEvent: (WorkoutUiEvent) -> Unit,
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        PreloadWorkoutImages(workouts = state.workouts)

        val currentWorkout = state.workouts.getOrNull(state.currentWorkoutIndex)
        currentWorkout?.let {
            WorkoutDetails(
                workout = it,
                onEvent = onEvent,
                state = state,
            )
        }
    }
}


@Composable
fun PreloadWorkoutImages(
    workouts: List<UiExercise>
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)

    LaunchedEffect(workouts) {
        workouts.forEach { workout ->
            val request = ImageRequest.Builder(context)
                .data(workout.workoutImage)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .allowHardware(false)
                .build()

            imageLoader.enqueue(request)
        }
    }
}

@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (WorkoutUiEvent) -> Unit,
    state: WorkoutUiState
) {
    val actions = listOf(
        TopBarAction(
            icon = if (state.isTTSActive) Icons.AutoMirrored.Default.VolumeUp else Icons.AutoMirrored.Default.VolumeOff,
            onClick = { onEvent(WorkoutUiEvent.OnToggleTTSClick) }
        )
    )

    LaunchedEffect(state.isTTSActive) {
        onSetupTopBar(
            TopBarState(
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(WorkoutUiEvent.OnBackClick) }
                ),
                actions = actions
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutContentPreview() {
    val sampleExercise = UiExercise(
        name = "Plank",
        description = "Hold the plank position for 60 seconds.",
        type = Exercise.Type.TIMED,
        value = 60,
        sets = 3,
        muscleGroups = listOf(Exercise.MuscleGroup.CORE, Exercise.MuscleGroup.SHOULDERS),
        workoutImage = "",
        position = 0
    )

    WorkoutContent(
        state = WorkoutUiState(workouts = listOf(sampleExercise)),
        onEvent = {},
    )
}

