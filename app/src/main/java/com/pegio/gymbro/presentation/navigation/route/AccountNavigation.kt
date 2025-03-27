package com.pegio.gymbro.presentation.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object AccountRoute

fun NavController.navigateToAccount(navOptions: NavOptions? = null) {
    navigate(route = AccountRoute, navOptions = navOptions)
}