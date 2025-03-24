package com.pegio.gymbro.presentation.drawer

sealed interface AppDrawerUiEffect {
    data object NavigateToAuth: AppDrawerUiEffect
}