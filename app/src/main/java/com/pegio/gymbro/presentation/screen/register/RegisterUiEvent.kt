package com.pegio.gymbro.presentation.screen.register

sealed interface RegisterUiEvent {
    data class OnUsernameChanged(val newValue: String) : RegisterUiEvent
    data object OnRegister: RegisterUiEvent
}