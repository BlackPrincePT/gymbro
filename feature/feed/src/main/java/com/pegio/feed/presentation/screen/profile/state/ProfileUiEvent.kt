package com.pegio.feed.presentation.screen.profile.state

import com.pegio.model.FollowRecord
import com.pegio.model.Vote

sealed interface ProfileUiEvent {

    // Main
    data class OnPostVote(val postId: String, val voteType: Vote.Type) : ProfileUiEvent
    data object OnLoadMorePosts : ProfileUiEvent
    data object OnFollowClick : ProfileUiEvent

    // Navigation
    data object OnBackClick : ProfileUiEvent
    data class OnFollowRecordClick(val userId: String, val mode: FollowRecord.Type) : ProfileUiEvent
}