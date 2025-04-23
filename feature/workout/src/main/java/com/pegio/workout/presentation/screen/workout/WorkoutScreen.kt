package com.pegio.workout.presentation.screen.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.pegio.model.Workout
import com.pegio.workout.presentation.component.WorkoutDetails
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
        onNextClick = { viewModel.onEvent(WorkoutUiEvent.OnNextClick) },
        onToggleTTSClick = { viewModel.onEvent(WorkoutUiEvent.OnToggleTTSClick) },
        onPreviousClick = { viewModel.onEvent(WorkoutUiEvent.OnPreviousClick) },
        onReadDescriptionClick = { textToRead ->
            viewModel.onEvent(WorkoutUiEvent.OnReadTTSClick(textToRead))
        },
        onStartTimer = { durationSeconds ->
            viewModel.onEvent(
                WorkoutUiEvent.StartTimer(
                    durationSeconds
                )
            )
        },
        onPauseTimer = { viewModel.onEvent(WorkoutUiEvent.PauseTimer) },
        onResumeTimer = { viewModel.onEvent(WorkoutUiEvent.ResumeTimer) },
    )
}

@Composable
fun WorkoutContent(
    state: WorkoutUiState,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onToggleTTSClick: () -> Unit,
    onReadDescriptionClick: (String) -> Unit,
    onStartTimer: (Int) -> Unit,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
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
                isTTSActive = state.isTTSActive,
                onNextClick = onNextClick,
                isLastWorkout = state.currentWorkoutIndex == state.workouts.size - 1,
                onToggleTTSClick = onToggleTTSClick,
                timeRemaining = state.timeRemaining,
                timerState = state.timerState,
                showBackButton = state.currentWorkoutIndex != 0,
                onPreviousClick = onPreviousClick,
                onReadDescriptionClick = onReadDescriptionClick,
                onStartTimer = onStartTimer,
                onPauseTimer = onPauseTimer,
                onResumeTimer = onResumeTimer,
            )
        }
    }
}


@Composable
fun PreloadWorkoutImages(
    workouts: List<UiWorkout>
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


@Preview(showBackground = true)
@Composable
fun WorkoutContentPreview() {
    val sampleWorkout = UiWorkout(
        name = "Plank",
        description = "Hold the plank position for 60 seconds.",
        workoutType = Workout.WorkoutType.TIMED,
        value = 60,
        sets = 3,
        isFinished = false,
        muscleGroups = listOf(Workout.MuscleGroup.CORE, Workout.MuscleGroup.SHOULDERS),
        workoutImage = ""
    )

    WorkoutContent(
        state = WorkoutUiState(workouts = listOf(sampleWorkout)),
        onNextClick = {},
        onPreviousClick = {},
        onToggleTTSClick = {},
        onReadDescriptionClick = {},
        onStartTimer = {},
        onPauseTimer = {},
        onResumeTimer = {}
    )
}

