package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import com.pegio.gymbro.navigation.popNavigate
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHome() = popNavigate(route = HomeRoute)