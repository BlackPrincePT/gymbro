package com.pegio.gymbro.presentation.navigation

import androidx.navigation.NavController

fun <T : Any> NavController.popNavigate(route: T) {
    navigate(
        route = route,
        builder = {
            popUpTo(graph.id) { inclusive = true }
            launchSingleTop = true
        }
    )
}