package com.pegio.gymbro.presentation.screen.account

sealed interface AccountUiEffect {
    data object NavigateBack : AccountUiEffect
}
