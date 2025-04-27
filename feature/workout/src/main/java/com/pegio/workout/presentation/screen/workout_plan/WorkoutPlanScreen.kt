package com.pegio.workout.presentation.screen.workout_plan

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.common.presentation.state.TopBarAction
import com.pegio.common.presentation.state.TopBarState
import com.pegio.common.presentation.util.CollectLatestEffect
import com.pegio.workout.presentation.component.TipCardComponents
import com.pegio.workout.presentation.component.WorkoutPlanItemComponents
import com.pegio.workout.presentation.model.UiWorkoutPlan
import com.pegio.workout.presentation.screen.workout_plan.state.WorkoutPlanUiEffect
import com.pegio.workout.presentation.screen.workout_plan.state.WorkoutPlanUiEvent
import com.pegio.workout.presentation.screen.workout_plan.state.WorkoutPlanUiState

@Composable
fun WorkoutPlanScreen(
    viewModel: WorkoutPlanViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onStartWorkout: (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {

    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    val context = LocalContext.current


    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is WorkoutPlanUiEffect.Failure -> onShowSnackbar(context.getString(effect.errorRes))
            is WorkoutPlanUiEffect.NavigateToWorkout -> onStartWorkout(effect.workoutId)
            WorkoutPlanUiEffect.NavigateBack -> onBackClick()
            WorkoutPlanUiEffect.NavigateToAiChat -> onInfoClick()
        }
    }


    WorkoutPlanContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier,
    )


}

@Composable
fun WorkoutPlanContent(
    state: WorkoutPlanUiState,
    onEvent: (WorkoutPlanUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(state.plans) { workoutPlan ->
            WorkoutPlanItemComponents(
                workoutPlan =  workoutPlan,
                onStartWorkout = { onEvent(WorkoutPlanUiEvent.StartWorkout(workoutPlan.workoutId)) }
            )
        }

        item {
            TipCardComponents(onClick = { onEvent(WorkoutPlanUiEvent.OnInfoClick) })
        }
    }
}


@Composable
private fun SetupTopBar(
    onSetupTopBar: (TopBarState) -> Unit,
    onEvent: (WorkoutPlanUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onSetupTopBar(
            TopBarState(
                title = "Workout plan",
                navigationIcon = TopBarAction(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = { onEvent(WorkoutPlanUiEvent.OnBackClick) }
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutPlanContentPreview() {
    val dummyPlans = listOf(
        UiWorkoutPlan(
            id = "1",
            title = "Beginner Plan",
            description = "Designed for those with some experience. Mix of strength and cardio to push your limits.",
            difficulty = "Easy",
            duration = "4 days/week - 45 minutes per session",
            intensity = "Low",
            imageUrl = "",
            workoutId = ""
        )
    )

    val dummyState = WorkoutPlanUiState(
        plans = dummyPlans,
    )

    WorkoutPlanContent(
        state = dummyState,
        onEvent = {},
        modifier = Modifier
    )
}
