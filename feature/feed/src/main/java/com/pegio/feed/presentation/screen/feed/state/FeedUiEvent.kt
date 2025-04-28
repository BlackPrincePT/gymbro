package com.pegio.feed.presentation.screen.feed.state

import com.pegio.model.Vote

sealed interface FeedUiEvent {

    // Main
    data object OnLoadMorePosts : FeedUiEvent
    data object OnPostsRefresh : FeedUiEvent
    data class OnPostVote(val postId: String, val voteType: Vote.Type) : FeedUiEvent

    // Navigation
    data object OnChatClick : FeedUiEvent
    data object OnDrawerClick : FeedUiEvent
    data class OnPostWorkoutClick(val workoutId: String) : FeedUiEvent
    data class OnCreatePostClick(val shouldOpenGallery: Boolean) : FeedUiEvent
    data class OnPostCommentClick(val postId: String) : FeedUiEvent
    data class OnUserProfileClick(val userId: String) : FeedUiEvent
}