package com.pegio.feed.presentation.screen.profile.state

sealed interface ProfileUiEvent {

    // Top Bar
    data object OnBackClick: ProfileUiEvent
}