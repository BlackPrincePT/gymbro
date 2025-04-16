package com.pegio.auth.presentation.screen.register

sealed interface RegisterUiEffect {
    data object NavigateToHome: RegisterUiEffect
}