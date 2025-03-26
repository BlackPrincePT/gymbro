package com.pegio.gymbro.presentation.screen.register

sealed interface RegisterUiEffect {
    data object NavigateToHome: RegisterUiEffect
}