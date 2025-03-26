package com.pegio.gymbro.presentation.screen.auth

import com.pegio.gymbro.domain.core.Error

sealed interface AuthUiEffect {
    data object NavigateToHome: AuthUiEffect
    data object NavigateToRegister: AuthUiEffect
    data class Failure(val e: Error) : AuthUiEffect
}