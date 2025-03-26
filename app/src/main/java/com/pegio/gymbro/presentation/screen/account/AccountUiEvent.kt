package com.pegio.gymbro.presentation.screen.account

sealed interface AccountUiEvent {
    data object OnProfileImageClicked: AccountUiEvent
}