package com.pegio.workout.presentation.screen.workoutplan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.workout.presentation.screen.workoutplan.WorkoutPlanScreen
import kotlinx.serialization.Serializable

@Serializable
data object WorkoutPlanRoute

fun NavController.navigateToWorkoutPlan() = navigate(route = WorkoutPlanRoute)

fun NavGraphBuilder.workoutPlanScreen(
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onStartWorkout: (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {
    composable<WorkoutPlanRoute> {
        WorkoutPlanScreen(
            onBackClick = onBackClick,
            onInfoClick = onInfoClick,
            onShowSnackbar = onShowSnackbar,
            onStartWorkout = onStartWorkout,
            onSetupTopBar = onSetupTopBar,
        )
    }
}