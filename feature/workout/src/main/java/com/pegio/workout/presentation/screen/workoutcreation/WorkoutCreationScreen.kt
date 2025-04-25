package com.pegio.workout.presentation.screen.workoutcreation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.workout.presentation.component.AddWorkoutDialog
import com.pegio.workout.presentation.component.WorkoutItem

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

        if (state.showAddWorkoutDialog) {
            AddWorkoutDialog(
                state = state,
                onEvent = onEvent,
            )
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