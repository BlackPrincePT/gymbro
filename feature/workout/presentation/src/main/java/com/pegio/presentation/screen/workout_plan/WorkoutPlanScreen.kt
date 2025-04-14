package com.pegio.presentation.screen.workout_plan

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.presentation.components.TipCardComponents
import com.pegio.presentation.components.WorkoutPlanItemComponents
import com.pegio.presentation.model.UiWorkoutPlan
import com.pegio.presentation.util.CollectLatestEffect


@Composable
fun WorkoutPlanScreen(
    viewModel: WorkoutPlanViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
//    onSetupTopBar: (TopBarState) -> Unit
) {

//    SetupTopBar(onSetupTopBar, viewModel::onEvent)

    LaunchedEffect(Unit) {
        viewModel.onEvent(WorkoutPlanUiEvent.LoadInitialPlans)
    }

    CollectLatestEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is WorkoutPlanUiEffect.Failure -> {}
            WorkoutPlanUiEffect.NavigateBack -> onBackClick()
            WorkoutPlanUiEffect.NavigateToAiChat -> onInfoClick()
        }
    }


    WorkoutPlanContent(
        state = viewModel.uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier
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
            WorkoutPlanItemComponents(workoutPlan)
        }

        item {
            TipCardComponents(onClick = { onEvent(WorkoutPlanUiEvent.OnInfoClick) })
        }
    }
}



//@Composable
//private fun SetupTopBar(
//    onSetupTopBar: (TopBarState) -> Unit,
//    onEvent: (WorkoutPlanUiEvent) -> Unit
//) {
//    LaunchedEffect(Unit) {
//        onSetupTopBar(
//            TopBarState(
//                navigationIcon = TopBarAction(
//                    icon = Icons.AutoMirrored.Default.ArrowBack,
//                    onClick = { onEvent(WorkoutPlanUiEvent.OnBackClick) }
//                )
//            )
//        )
//    }
//}

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
            imageUrl = ""
        ),
        UiWorkoutPlan(
            id = "1",
            title = "Beginner Plan",
            description = "Designed for those with some experience. Mix of strength and cardio to push your limits.",
            difficulty = "Easy",
            duration = "4 days/week - 45 minutes per session",
            intensity = "Low",
            imageUrl = ""
        ),
        UiWorkoutPlan(
            id = "1",
            title = "Beginner Plan",
            description = "Designed for those with some experience. Mix of strength and cardio to push your limits.",
            difficulty = "Easy",
            duration = "4 days/week - 45 minutes per session",
            intensity = "Low",
            imageUrl = ""
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
