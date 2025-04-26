package com.pegio.settings.presentation.screen.account.state

sealed interface AccountUiEffect {

    // Main
    data object LaunchGallery: AccountUiEffect

    // Navigation
    data object NavigateBack : AccountUiEffect
}
