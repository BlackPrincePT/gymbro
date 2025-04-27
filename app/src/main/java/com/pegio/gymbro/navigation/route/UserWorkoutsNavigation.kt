package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object UserWorkoutsRoute

fun NavController.navigateToUser(navOptions: NavOptions? = null) {
    navigate(route = UserWorkoutsRoute, navOptions = navOptions)
}