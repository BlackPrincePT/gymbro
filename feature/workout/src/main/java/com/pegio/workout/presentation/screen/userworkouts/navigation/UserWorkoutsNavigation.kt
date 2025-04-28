package com.pegio.workout.presentation.screen.userworkouts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pegio.common.presentation.state.TopBarState
import com.pegio.workout.presentation.screen.userworkouts.UserWorkoutsScreen
import kotlinx.serialization.Serializable

@Serializable
data class UserWorkoutsRoute(val isChoosing: Boolean)

fun NavController.navigateToUsersWorkouts(isChoosing: Boolean = false) {
    navigate(route = UserWorkoutsRoute(isChoosing))
}

fun NavGraphBuilder.userWorkoutsScreen(
    onBackClick: (String?) -> Unit,
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
