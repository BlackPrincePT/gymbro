package com.pegio.gymbro.presentation.screen.home

sealed interface HomeUiEvent {
    data object OnAccountClick: HomeUiEvent
    data object OnSignOut : HomeUiEvent
}