package com.pegio.settings.presentation.screen.settings.state

sealed interface SettingsUiEffect {

    // Navigation
    data object NavigateBack: SettingsUiEffect
}