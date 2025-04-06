package com.pegio.gymbro.presentation.navigation.route

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data object CreatePostRoute

fun NavController.navigateToCreatePost() = navigate(CreatePostRoute)