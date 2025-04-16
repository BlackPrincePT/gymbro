package com.pegio.auth.presentation.screen.auth

import com.pegio.common.core.Error

sealed interface AuthUiEffect {
    data object NavigateToHome: AuthUiEffect
    data object NavigateToRegister: AuthUiEffect
    data class Failure(val error: Error) : AuthUiEffect
}