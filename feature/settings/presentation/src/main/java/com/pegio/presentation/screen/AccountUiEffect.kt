package com.pegio.presentation.screen

sealed interface AccountUiEffect {
    data object NavigateBack : AccountUiEffect
}
