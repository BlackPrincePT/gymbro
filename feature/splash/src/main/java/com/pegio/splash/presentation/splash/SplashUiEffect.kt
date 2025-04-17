package com.pegio.splash.presentation.splash

internal sealed interface SplashUiEffect {
    data object NavigateToHome: SplashUiEffect
    data object NavigateToAuth: SplashUiEffect
    data object NavigateToRegister: SplashUiEffect
}