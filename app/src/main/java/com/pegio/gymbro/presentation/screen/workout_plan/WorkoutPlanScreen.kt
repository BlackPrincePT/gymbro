package com.pegio.gymbro.presentation.screen.workout_plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pegio.gymbro.presentation.model.UiWorkoutPlan

@Composable
fun WorkoutPlanScreen(
    viewModel: WorkoutPlanViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.onEvent(WorkoutPlanUiEvent.LoadInitialPlans)
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
    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.plans) { workoutPlan ->
                WorkoutPlanItem(workoutPlan)
            }
        }
    }
}

@Composable
fun WorkoutPlanItem(workoutPlan: UiWorkoutPlan) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = workoutPlan.title)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Difficulty: ${workoutPlan.difficulty}")
            Text(text = "Duration: ${workoutPlan.duration}")
            Text(text = "Intensity: ${workoutPlan.intensity}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = workoutPlan.description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutPlanContentPreview() {
    val dummyPlans = listOf(
        UiWorkoutPlan(
            id = "1",
            title = "Beginner Plan",
            description = "Great for starters.",
            difficulty = "Easy",
            duration = "4 weeks",
            intensity = "Low",
            imageUrl = ""
        ),
        UiWorkoutPlan(
            id = "2",
            title = "Advanced Plan",
            description = "Challenge yourself.",
            difficulty = "Hard",
            duration = "8 weeks",
            intensity = "High",
            imageUrl = ""
        ),
        UiWorkoutPlan(
            id = "2",
            title = "Advanced Plan",
            description = "Challenge yourself.",
            difficulty = "Hard",
            duration = "8 weeks",
            intensity = "High",
            imageUrl = ""
        )
    )

    val dummyState = WorkoutPlanUiState(
        plans = dummyPlans,
        isLoading = false,
    )

    WorkoutPlanContent(
        state = dummyState,
        onEvent = {},
        modifier = Modifier
    )
}
