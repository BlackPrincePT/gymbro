package com.pegio.gymbro.presentation.register

sealed interface RegisterUiEffect {
    data object NavigateToHome: RegisterUiEffect
}