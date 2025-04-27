package com.pegio.settings.presentation.screen.settings.state

import com.pegio.designsystem.theme.ThemeMode

sealed interface SettingsUiEvent {

    // Main
    data class OnThemeSelected(val mode: ThemeMode) : SettingsUiEvent

    // Navigation
    data object OnBackClick : SettingsUiEvent
}