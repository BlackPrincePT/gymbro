package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRoute(val workoutPlanId: String)

fun NavController.navigateToWorkout(workoutPlanId: String, navOptions: NavOptions? = null) {
    navigate(route = WorkoutRoute(workoutPlanId), navOptions = navOptions)
}