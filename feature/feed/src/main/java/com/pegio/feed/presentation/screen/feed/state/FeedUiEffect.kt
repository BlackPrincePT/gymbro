package com.pegio.feed.presentation.screen.feed.state

import androidx.annotation.StringRes

sealed interface FeedUiEffect {

    // Failure
    data class ShowSnackbar(@StringRes val errorRes: Int) : FeedUiEffect

    // Navigation
    data object OpenDrawer : FeedUiEffect
    data object NavigateToChat : FeedUiEffect
    data class NavigateToCreatePost(val shouldOpenGallery: Boolean) : FeedUiEffect
    data class NavigateToPostDetails(val postId: String) : FeedUiEffect
    data class NavigateToUserProfile(val userId: String) : FeedUiEffect
}