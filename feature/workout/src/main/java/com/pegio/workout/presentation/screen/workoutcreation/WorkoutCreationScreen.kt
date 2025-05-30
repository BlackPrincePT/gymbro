package com.pegio.workout.presentation.screen.workoutcreation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.common.presentation.util.rememberGalleryLauncher
import com.pegio.designsystem.component.FormTextField
import com.pegio.workout.R
import com.pegio.workout.presentation.component.AddWorkoutDialog
import com.pegio.workout.presentation.component.WorkoutItem
import com.pegio.workout.presentation.model.UiExercise
import com.pegio.workout.presentation.screen.workoutcreation.state.WorkoutCreationUiEffect
import com.pegio.workout.presentation.screen.workoutcreation.state.WorkoutCreationUiEvent
import com.pegio.workout.presentation.screen.workoutcreation.state.WorkoutCreationUiState

@Composable
fun WorkoutCreationScreen(
    viewModel: WorkoutCreationViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {

    val launchGallery = rememberGalleryLauncher(
        onImageSelected = { viewModel.onEvent(WorkoutCreationUiEvent.OnWorkoutImageSelected(it)) }
    )

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    val context = LocalContext.current

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is WorkoutCreationUiEffect.Failure -> onShowSnackbar(context.getString(effect.errorRes))
            WorkoutCreationUiEffect.NavigateBack -> onBackClick()
            WorkoutCreationUiEffect.LaunchGallery -> launchGallery()
        }
    }

    WorkoutCreationContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun WorkoutCreationContent(
    state: WorkoutCreationUiState,
    onEvent: (WorkoutCreationUiEvent) -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            FormTextField(
                value = state.title,
                onValueChange = { onEvent(WorkoutCreationUiEvent.OnTitleChange(it)) },
                label = stringResource(R.string.feature_workout_title),
                error = state.validationError.title
            )
            Spacer(modifier = Modifier.height(8.dp))
            FormTextField(
                value = state.description,
                onValueChange = { onEvent(WorkoutCreationUiEvent.OnDescriptionChange(it)) },
                label = stringResource(R.string.feature_workout_description),
                error = state.validationError.mainDescription
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.exercises) { workout ->
                    WorkoutItem(
                        workout = workout,
                        onEvent = onEvent
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    onEvent(WorkoutCreationUiEvent.OnAddWorkoutClick)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.feature_workout_add_workout))
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
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
                ),
                actions = listOf(TopBarAction(
                    icon = Icons.Default.Upload,
                    onClick = { onEvent(WorkoutCreationUiEvent.OnUploadWorkouts) }
                ))
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWorkoutCreationContent() {
    val state = WorkoutCreationUiState(
        exercises = listOf(
            UiExercise.EMPTY,
            UiExercise.EMPTY
        ),
    )

    WorkoutCreationContent(
        state = state,
        onEvent = {},
    )
}