package com.pegio.workout.presentation.screen.userworkouts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.workout.presentation.screen.userworkouts.UserWorkoutsScreen
import kotlinx.serialization.Serializable

@Serializable
data object UserWorkoutsRoute

fun NavController.navigateToUsersWorkouts() = navigate(route = UserWorkoutsRoute)

fun NavGraphBuilder.userWorkoutsScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onStartWorkout: (String) -> Unit,
    onCreateWorkoutClick: () -> Unit,
    onSetupTopBar: (TopBarState) -> Unit
) {
    composable<UserWorkoutsRoute> {
        UserWorkoutsScreen(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
            onStartWorkout = onStartWorkout,
            onCreateWorkoutClick = onCreateWorkoutClick,
            onSetupTopBar = onSetupTopBar
        )
    }
}
