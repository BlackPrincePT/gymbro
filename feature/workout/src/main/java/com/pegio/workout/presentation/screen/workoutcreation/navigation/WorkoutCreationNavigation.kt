package com.pegio.workout.presentation.screen.workoutcreation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.workout.presentation.screen.workoutcreation.WorkoutCreationScreen
import kotlinx.serialization.Serializable

@Serializable
data object WorkoutCreationRoute

fun NavController.navigateToWorkoutCreation() = navigate(route =  WorkoutCreationRoute)

fun NavGraphBuilder.workoutCreationScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {
    composable<WorkoutCreationRoute> {
        WorkoutCreationScreen(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
            onSetupTopBar = onSetupTopBar,
        )
    }
}
