package com.pegio.gymbro.navigation.route

import androidx.navigation.NavController
import com.pegio.common.presentation.util.popNavigate
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

fun NavController.navigateToMain() = popNavigate(MainRoute)