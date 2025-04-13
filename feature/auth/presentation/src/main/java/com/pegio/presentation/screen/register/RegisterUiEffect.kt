package com.pegio.presentation.screen.register

sealed interface RegisterUiEffect {
    data object NavigateToHome: RegisterUiEffect
}