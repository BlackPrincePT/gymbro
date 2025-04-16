package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object WorkoutPlanRoute

fun NavController.navigateToWorkoutPlan(navOptions: NavOptions? = null) {
    navigate(route = WorkoutPlanRoute, navOptions = navOptions)
}