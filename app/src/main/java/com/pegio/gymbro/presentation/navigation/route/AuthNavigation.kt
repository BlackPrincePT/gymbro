package com.pegio.gymbro.presentation.navigation.route

import androidx.navigation.NavController
import com.pegio.gymbro.presentation.navigation.popNavigate
import kotlinx.serialization.Serializable

@Serializable
data object AuthRoute

fun NavController.navigateToAuth() = popNavigate(route = AuthRoute)
