package com.pegio.feed.presentation.screen.feed.state

sealed interface FeedUiEvent {
    data object OnCreatePostClick: FeedUiEvent
    data object OnLoadMorePosts: FeedUiEvent

    // Top Bar
    data object OnChatClick: FeedUiEvent
    data object OnDrawerClick: FeedUiEvent
}