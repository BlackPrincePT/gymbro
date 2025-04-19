package com.pegio.feed.presentation.screen.feed.state

sealed interface FeedUiEvent {

    // Main
    data object OnLoadMorePosts: FeedUiEvent
    data object OnPostsRefresh: FeedUiEvent

    // Top Bar
    data object OnChatClick: FeedUiEvent
    data object OnDrawerClick: FeedUiEvent

    // Navigation
    data object OnCreatePostClick: FeedUiEvent
    data class OnPostCommentClick(val postId: String): FeedUiEvent
}