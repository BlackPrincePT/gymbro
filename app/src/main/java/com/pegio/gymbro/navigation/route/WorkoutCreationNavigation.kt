package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object WorkoutCreationRoute

fun NavController.navigateToWorkoutCreation(navOptions: NavOptions? = null) {
    navigate(route = WorkoutCreationRoute, navOptions = navOptions)
}