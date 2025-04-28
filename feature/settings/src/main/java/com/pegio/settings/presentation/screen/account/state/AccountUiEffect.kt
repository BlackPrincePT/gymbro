package com.pegio.settings.presentation.screen.account.state

sealed interface AccountUiEffect {

    // Main
    data object LaunchGallery : AccountUiEffect
    data object ClearFocus : AccountUiEffect

    // Navigation
    data object NavigateBack : AccountUiEffect
}
