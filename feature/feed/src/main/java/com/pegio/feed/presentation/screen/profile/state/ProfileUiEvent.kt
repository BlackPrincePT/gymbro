package com.pegio.feed.presentation.screen.profile.state

import com.pegio.model.Vote

sealed interface ProfileUiEvent {

    // Main
    data class OnPostVote(val postId: String, val voteType: Vote.Type) : ProfileUiEvent
    data object OnLoadMorePosts: ProfileUiEvent

    // Top Bar
    data object OnBackClick: ProfileUiEvent
}