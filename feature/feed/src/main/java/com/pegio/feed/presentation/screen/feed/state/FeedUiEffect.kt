package com.pegio.feed.presentation.screen.feed.state

sealed interface FeedUiEffect {
    data object NavigateToCreatePost : FeedUiEffect

    // Top Bar
    data object OpenDrawer : FeedUiEffect
    data object NavigateToChat : FeedUiEffect
}