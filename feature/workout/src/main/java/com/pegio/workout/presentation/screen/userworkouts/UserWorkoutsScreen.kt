package com.pegio.workout.presentation.screen.userworkouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.designsystem.component.GymBroTextButton
import com.pegio.workout.R
import com.pegio.workout.presentation.model.UiWorkout
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiEffect
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiEvent
import com.pegio.workout.presentation.screen.userworkouts.state.UserWorkoutsUiState

@Composable
fun UserWorkoutsScreen(
    viewModel: UserWorkoutsViewModel = hiltViewModel(),
    onBackClick: (String?) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onStartWorkout: (String) -> Unit,
    onCreateWorkoutClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {

    SetupTopBar(
        stringResource(R.string.feature_workout_my_workouts),
        onSetupTopBar,
        viewModel::onEvent
    )

    val context = LocalContext.current

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is UserWorkoutsUiEffect.Failure -> onShowSnackbar(context.getString(effect.errorRes))
            is UserWorkoutsUiEffect.NavigateBack -> onBackClick(effect.selectedWorkoutId)
            is UserWorkoutsUiEffect.NavigateToWorkout -> onStartWorkout(effect.workoutId)
            UserWorkoutsUiEffect.NavigateToWorkoutCreation -> onCreateWorkoutClick()
        }
    }

    UserWorkoutsContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier
    )
}

@Composable
fun UserWorkoutsContent(
    state: UserWorkoutsUiState,
    onEvent: (UserWorkoutsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(state.workouts) { workout ->
            UserWorkoutItem(
                workout = workout,
                isChoosing = state.isChoosing,
                onChooseClick = { onEvent(UserWorkoutsUiEvent.OnBackClick(workout.id)) },
                onStartWorkout = { onEvent(UserWorkoutsUiEvent.StartWorkout(workout.id)) }
            )
        }
    }
}

@Composable
fun UserWorkoutItem(
    workout: UiWorkout,
    isChoosing: Boolean,
    onChooseClick: () -> Unit,
    onStartWorkout: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onStartWorkout),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workout.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = workout.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (isChoosing) GymBroTextButton(
                text = stringResource(R.string.feature_workout_choose),
                onClick = onChooseClick
            )
        }
    }
}


@Composable
private fun SetupTopBar(
    title: String,
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (UserWorkoutsUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                title = title,
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(UserWorkoutsUiEvent.OnBackClick(selectedWorkoutId = null)) }
                ),
                actions = listOf(TopBarAction(
                    icon = Icons.Default.Add,
                    onClick = { onEvent(UserWorkoutsUiEvent.OnCreateWorkoutClick) }
                ))
            )
        )
    }
}