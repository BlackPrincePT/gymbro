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
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.workout.presentation.component.WorkoutDetails


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
        }
    )
}

@Composable
fun WorkoutContent(
    state: WorkoutUiState,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onToggleTTSClick: () -> Unit,
    onReadDescriptionClick: (String) -> Unit
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
                onPreviousClick = onPreviousClick,
                onToggleClick = onToggleTTSClick,
                isLastWorkout = state.currentWorkoutIndex == state.workouts.size - 1,
                showBackButton = state.currentWorkoutIndex != 0,
                isTTSActive = state.isTTSActive,
                onReadDescriptionClick = onReadDescriptionClick,
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
