package com.pegio.feed.presentation.screen.postdetails.state

import androidx.annotation.StringRes

sealed interface PostDetailsUiEffect {

    // Failure
    data class ShowSnackbar(@StringRes val errorRes: Int) : PostDetailsUiEffect

    // Navigation
    data object NavigateBack : PostDetailsUiEffect
    data class NavigateToUserProfile(val userId: String) : PostDetailsUiEffect
}