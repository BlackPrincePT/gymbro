package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import com.pegio.common.presentation.util.popNavigate
import kotlinx.serialization.Serializable

@Serializable
data object RegisterRoute

fun NavController.navigateToRegister() = popNavigate(route = RegisterRoute)