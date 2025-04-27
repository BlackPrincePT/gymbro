package com.pegio.auth.presentation.screen.register.state

sealed interface RegisterUiEffect {

    // Main
    data object LaunchGallery: RegisterUiEffect

    // Navigation
    data object NavigateToHome: RegisterUiEffect
}