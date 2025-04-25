package com.pegio.feed.presentation.screen.createpost.state

import androidx.annotation.StringRes

sealed interface CreatePostUiEffect {

    // Main
    data object LaunchGallery : CreatePostUiEffect

    // Failure
    data class ShowSnackbar(@StringRes val errorRes: Int): CreatePostUiEffect

    // Navigation
    data object NavigateBack: CreatePostUiEffect
}