package com.pegio.feed.presentation.screen.postdetails.state

sealed interface PostDetailsUiEvent {

    // TopBar
    data object OnBackClick : PostDetailsUiEvent

    // Comment
    data class OnCommentTextChange(val value: String) : PostDetailsUiEvent
    data object OnCommentSubmit : PostDetailsUiEvent
}