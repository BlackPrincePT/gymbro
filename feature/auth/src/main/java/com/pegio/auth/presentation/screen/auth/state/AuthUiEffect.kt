package com.pegio.auth.presentation.screen.auth.state

import androidx.annotation.StringRes

sealed interface AuthUiEffect {
    data object NavigateToRegister : AuthUiEffect
    data object NavigateToHome : AuthUiEffect
    data class ShowSnackbar(@StringRes val errorRes: Int) : AuthUiEffect
}