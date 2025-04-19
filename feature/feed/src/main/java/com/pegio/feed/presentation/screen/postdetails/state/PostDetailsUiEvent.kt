package com.pegio.feed.presentation.screen.postdetails.state

sealed interface PostDetailsUiEvent {

    // TopBar
    data object OnBackClick : PostDetailsUiEvent

    // Main
    data object OnCommentSubmitClick : PostDetailsUiEvent
    data object OnLoadMoreCommentsClick : PostDetailsUiEvent

    // Compose State
    data class OnCommentTextChange(val value: String) : PostDetailsUiEvent
}