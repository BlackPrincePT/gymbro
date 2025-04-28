package com.pegio.settings.presentation.screen.settings.state

import com.pegio.designsystem.theme.ThemeMode

data class SettingsUiState(

    // Loading
    val isLoading: Boolean = false,

    // Compose state
    val selectedMode: ThemeMode? = null,
    val isThemeMenuExpanded: Boolean = false
)