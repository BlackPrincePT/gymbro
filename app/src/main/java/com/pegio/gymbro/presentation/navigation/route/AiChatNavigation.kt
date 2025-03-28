package com.pegio.gymbro.presentation.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
data object AiChatRoute

fun NavController.navigateToAiChat(navOptions: NavOptions? = null) {
    navigate(route = AiChatRoute, navOptions = navOptions)
}