package com.pegio.feed.presentation.screen.postdetails.state

sealed interface PostDetailsUiEffect {

    // Navigation
    data object NavigateBack : PostDetailsUiEffect
    data class NavigateToUserProfile(val userId: String) : PostDetailsUiEffect
}