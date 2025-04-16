package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import com.pegio.gymbro.navigation.popNavigate
import kotlinx.serialization.Serializable

@Serializable
data object RegisterRoute

fun NavController.navigateToRegister() = popNavigate(route = RegisterRoute)