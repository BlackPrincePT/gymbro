package com.pegio.auth.presentation.screen.auth.state

import com.pegio.common.core.Error

sealed interface AuthUiEffect {
    data object NavigateToRegister: AuthUiEffect
    data object NavigateToHome: AuthUiEffect
    data class Failure(val error: Error) : AuthUiEffect
}