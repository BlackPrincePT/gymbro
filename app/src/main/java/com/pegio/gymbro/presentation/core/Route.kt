package com.pegio.gymbro.presentation.core

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object SplashScreen : Route
    @Serializable data object AuthScreen: Route
    @Serializable data object RegisterScreen: Route
    @Serializable data object HomeScreen: Route
}