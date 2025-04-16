package com.pegio.settings.presentation.screen

sealed interface AccountUiEffect {
    data object NavigateBack : AccountUiEffect
}
