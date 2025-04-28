package com.pegio.workout.presentation.screen.workout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.workout.presentation.screen.workout.WorkoutScreen
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRoute(val workoutId: String)

fun NavController.navigateToWorkout(workoutId: String) = navigate(route = WorkoutRoute(workoutId))


fun NavGraphBuilder.workoutScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {
    composable<WorkoutRoute> {
        WorkoutScreen(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
            onSetupTopBar = onSetupTopBar,
        )
    }
}
