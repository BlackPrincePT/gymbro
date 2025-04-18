package com.pegio.feed.presentation.screen.postdetails.state

sealed interface PostDetailsUiEvent {

    // TopBar
    data object OnBackClick : PostDetailsUiEvent
}