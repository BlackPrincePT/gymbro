package com.pegio.feed.presentation.screen.feed

sealed interface HomeUiEffect {
    data object NavigateToCreatePost : HomeUiEffect

    // Top Bar
    data object OpenDrawer : HomeUiEffect
    data object NavigateToChat : HomeUiEffect
}