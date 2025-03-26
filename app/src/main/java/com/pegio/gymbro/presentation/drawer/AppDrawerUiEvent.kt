package com.pegio.gymbro.presentation.drawer

sealed interface AppDrawerUiEvent {
    data object OnSignOut: AppDrawerUiEvent
    data object OnAccountClicked: AppDrawerUiEvent
}