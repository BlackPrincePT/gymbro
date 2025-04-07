package com.pegio.gymbro.presentation.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data object WorkoutPlanRoute

fun NavController.navigateToWorkoutPlan() = navigate(WorkoutPlanRoute)