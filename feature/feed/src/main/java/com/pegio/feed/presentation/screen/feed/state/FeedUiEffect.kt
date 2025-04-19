package com.pegio.feed.presentation.screen.feed.state

sealed interface FeedUiEffect {

    // Top Bar
    data object OpenDrawer : FeedUiEffect
    data object NavigateToChat : FeedUiEffect

    // Navigation
    data object NavigateToCreatePost : FeedUiEffect
    data class NavigateToPostDetails(val postId: String) : FeedUiEffect
}