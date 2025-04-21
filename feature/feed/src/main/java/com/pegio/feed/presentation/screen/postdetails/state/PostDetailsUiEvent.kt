package com.pegio.feed.presentation.screen.postdetails.state

import com.pegio.model.Vote

sealed interface PostDetailsUiEvent {

    // TopBar
    data object OnBackClick : PostDetailsUiEvent

    // Main
    data object OnCommentSubmitClick : PostDetailsUiEvent
    data object OnLoadMoreCommentsClick : PostDetailsUiEvent
    data class OnPostVote(val voteType: Vote.Type) : PostDetailsUiEvent

    // Navigation
    data class OnUserProfileClick(val userId: String) : PostDetailsUiEvent

    // Compose State
    data class OnCommentTextChange(val value: String) : PostDetailsUiEvent
}