package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object WorkoutRoute

fun NavController.navigateToWorkout(navOptions: NavOptions? = null) {
    navigate(route = WorkoutRoute, navOptions = navOptions)
}