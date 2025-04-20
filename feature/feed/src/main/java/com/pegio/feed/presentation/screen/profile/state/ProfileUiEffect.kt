package com.pegio.feed.presentation.screen.profile.state

sealed interface ProfileUiEffect {

    // Navigation
    data object NavigateBack: ProfileUiEffect
}